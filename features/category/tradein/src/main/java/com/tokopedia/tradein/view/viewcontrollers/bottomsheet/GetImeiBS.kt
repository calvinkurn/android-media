package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tradein.R
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp

class GetImeiBS(val vm: TradeInHomeViewModel) : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var contentView: View? = null
    private var etWrapper: TextAreaUnify? = null

    companion object {
        @JvmStatic
        fun newInstance(vm: TradeInHomeViewModel): GetImeiBS {
            return GetImeiBS(vm)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setUpObserver()
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpObserver() {
        vm.imeiResponseLiveData.observe(viewLifecycleOwner, Observer {
            setWrongImei(it)
        })
    }

    private fun initLayout() {
        setTitle(getString(R.string.tradein_imei_title))
        isDragable = true
        isHideable = true
        isKeyboardOverlap = false
        customPeekHeight = (getScreenHeight()).toDp()
        showCloseIcon = false
        showKnob = true
        contentView = View.inflate(context,
                R.layout.tradein_bs_get_imei, null)

        val btnContinue = contentView?.findViewById<UnifyButton>(R.id.btn_continue)
        etWrapper = contentView?.findViewById(R.id.wrapper_imei)
        etWrapper?.textAreaWrapper?.helperText = "Tekan *#06# untuk cek IMEI atau dengan cara berikut";
        etWrapper?.textAreaInput?.inputType = InputType.TYPE_CLASS_NUMBER

        btnContinue?.setOnClickListener {
            when {
                etWrapper?.textAreaInput?.text.toString().isEmpty() -> {
                    etWrapper?.isError = true
                    etWrapper?.textAreaMessage = "Kamu belum memasukkan no. IMEI"
                }

                etWrapper?.textAreaInput?.text.toString().length != 15 -> {
                    etWrapper?.isError = true
                    etWrapper?.textAreaMessage = "No. IMEI kamu salah"
                }

                etWrapper?.textAreaInput?.text.toString().length == 15 -> {
                    vm.onInitialPriceClick(etWrapper?.textAreaInput?.text.toString())
                    dismiss()
                }
            }
        }
        setChild(contentView)
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onCourierButtonClick(shipperName: String?, price: String?)
    }

    private fun setWrongImei(error: String?) {
        etWrapper?.isError = true
        when (error) {
            getString(R.string.tradein_laku6_imei_error) -> {
                etWrapper?.textAreaMessage = getString(R.string.wrong_imei_string)
            }

            getString(R.string.tradein_laku6_imei_cheat) -> {
                etWrapper?.textAreaMessage = getString(R.string.tradein_wrong_imei_string)
            }

            else -> {
                etWrapper?.textAreaMessage = error ?: getString(R.string.wrong_imei_string)
            }
        }
    }
}