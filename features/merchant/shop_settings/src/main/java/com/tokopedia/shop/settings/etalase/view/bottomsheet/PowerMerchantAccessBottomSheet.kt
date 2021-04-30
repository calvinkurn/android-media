package com.tokopedia.shop.settings.etalase.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.PowerMerchantAccessModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class PowerMerchantAccessBottomSheet: BottomSheetUnify() {

    companion object {
        private const val MODEL = "model"

        @JvmStatic
        fun newInstance(model: PowerMerchantAccessModel): PowerMerchantAccessBottomSheet {
            return PowerMerchantAccessBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(MODEL, model)
                arguments = bundle
            }
        }
    }

    private var listener: BottomSheetListener? = null
    private var contentView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.fragment_power_merchant_access, null)
        setChild(contentView)
    }

    private fun setData() {
        val model = arguments?.getParcelable(MODEL) ?: PowerMerchantAccessModel()
        contentView?.apply {
            val imageViewIcon = findViewById<ImageView>(R.id.image_view_icon)
            val textViewTitle = findViewById<Typography>(R.id.text_view_title)
            val textViewDescription = findViewById<Typography>(R.id.text_view_description)
            val buttonRedirectTo = findViewById<UnifyButton>(R.id.button_redirect_to)

            model.let {
                imageViewIcon?.loadImage(it.imageUrl)
                textViewTitle?.text = MethodChecker.fromHtml(it.title)
                textViewDescription?.text = MethodChecker.fromHtml(it.desc)
                buttonRedirectTo?.text = it.btnTitle
                buttonRedirectTo?.setOnClickListener {
                    listener?.onBottomSheetButtonClicked()
                }
            }
        }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    interface BottomSheetListener {
        fun onBottomSheetButtonClicked()
    }
}