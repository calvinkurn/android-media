package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.viewmodel.TradeInImeiBSViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import javax.inject.Inject

class TradeInImeiBS() : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var contentView: View? = null
    private var etWrapper: TextAreaUnify? = null

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInImeiBSViewModel

    companion object {
        private const val LAKU6_DEVICE_MODEL = "LAKU6_DEVICE_MODEL"

        @JvmStatic
        fun newInstance(laku6DeviceModel: Laku6DeviceModel?): TradeInImeiBS {
            return TradeInImeiBS().also {
                it.arguments = Bundle().apply {
                    putParcelable(LAKU6_DEVICE_MODEL, laku6DeviceModel)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelProvider).get(TradeInImeiBSViewModel::class.java)
        setUpObserver()
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
        DaggerTradeInComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    private fun setUpObserver() {
        viewModel.tradeInImeiLiveData.observe(viewLifecycleOwner, Observer {
            it.validateImei.apply {
                if (isValid) {
                    actionListener?.onImeiButtonClick(etWrapper?.textAreaInput?.text.toString())
                    dismiss()
                } else {
                    etWrapper?.textAreaInput?.isEnabled = true
                    setWrongImei(message)
                }
            }
        })
        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if(it){
                view?.findViewById<View>(R.id.loader_parent)?.show()
            } else {
                view?.findViewById<View>(R.id.loader_parent)?.hide()
            }
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

        arguments?.getParcelable<Laku6DeviceModel>(LAKU6_DEVICE_MODEL)?.apply {
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
                        etWrapper?.textAreaInput?.isEnabled = false
                        viewModel.validateImei(
                            this,
                            etWrapper?.textAreaInput?.text.toString()
                        )
                    }
                }
            }
        }
        setChild(contentView)
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onImeiButtonClick(imei: String)
    }

    private fun setWrongImei(error: String?) {
        etWrapper?.isError = true
        etWrapper?.textAreaMessage = error ?: getString(R.string.wrong_imei_string)
    }
}