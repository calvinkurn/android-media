package com.tokopedia.shop.settings.etalase.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.constant.GMParamConstant
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.PowerMerchantAccessModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class PowerMerchantAccessBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var buttonRedirectTo: Button? = null
    private var imageViewIcon: ImageView? = null
    private var textViewTitle: Typography? = null
    private var textViewDescription: Typography? = null
    private var model: PowerMerchantAccessModel? = null
    private var listener: BottomSheetListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking(UserSession(context))
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initChildLayout()
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.fragment_power_merchant_access, null)
        contentView?.let { contentView ->
            imageViewIcon = contentView.findViewById(R.id.image_view_icon)
            buttonRedirectTo = contentView.findViewById(R.id.button_redirect_to)
            textViewTitle = contentView.findViewById(R.id.text_view_title)
            textViewDescription = contentView.findViewById(R.id.text_view_description)

            model?.let {
                imageViewIcon?.loadImage(it.imageUrl)
                textViewTitle?.text = MethodChecker.fromHtml(it.title)
                textViewDescription?.text = MethodChecker.fromHtml(it.desc)
                buttonRedirectTo?.text = it.btnTitle
                buttonRedirectTo?.setOnClickListener {
                    when (model?.trackingFlag) {
                        GMParamConstant.PM_HOME_NONACTIVE -> powerMerchantTracking.eventIncreaseScoreBottomSheet()
                        GMParamConstant.PM_SUBSCRIBE_SUCCESS -> powerMerchantTracking.eventLearnMoreSuccessPopUp()
                    }
                    listener?.onBottomSheetButtonClicked()
                }
            }
        }
        setChild(contentView)
    }

    private fun initVar() {
        model = arguments?.getParcelable(MODEL) ?: PowerMerchantAccessModel()
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    interface BottomSheetListener {
        fun onBottomSheetButtonClicked()
    }
}