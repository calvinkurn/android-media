package com.tokopedia.gm.common.widget

import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.GMParamConstant.PM_HOME_NONACTIVE
import com.tokopedia.gm.common.constant.GMParamConstant.PM_SUBSCRIBE_SUCCESS
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.loadImage

class MerchantCommonBottomSheet : BottomSheets() {

    private lateinit var buttonRedirectTo: Button
    private lateinit var imageViewIcon: ImageView
    private lateinit var textViewTitle: TextViewCompat
    private lateinit var textViewDescription: TextViewCompat
    private lateinit var model: BottomSheetModel
    private var listener: BottomSheetListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking()
    }

    companion object {
        private const val MODEL = "model"

        @JvmStatic
        fun newInstance(model: BottomSheetModel): MerchantCommonBottomSheet {
            return MerchantCommonBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(MODEL, model)
                arguments = bundle
            }
        }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.merchant_common_bottom_sheet
    }

    override fun initView(view: View) {
        initVar()
        imageViewIcon = view.findViewById(R.id.image_view_icon)
        buttonRedirectTo = view.findViewById(R.id.button_redirect_to)
        textViewTitle = view.findViewById(R.id.text_view_title)
        textViewDescription = view.findViewById(R.id.text_view_description)

        imageViewIcon.loadImage(model.imageUrl)
        textViewTitle.text = MethodChecker.fromHtml(model.title)
        textViewDescription.text = MethodChecker.fromHtml(model.desc)

        buttonRedirectTo.text = model.btnTitle
        buttonRedirectTo.setOnClickListener {
            when (model.trackingFlag) {
                PM_HOME_NONACTIVE -> powerMerchantTracking.eventIncreaseScoreBottomSheet()
                PM_SUBSCRIBE_SUCCESS -> powerMerchantTracking.eventLearnMoreSuccessPopUp()
            }
            listener?.onBottomSheetButtonClicked()
        }
    }

    private fun initVar() {
        model = arguments?.getParcelable(MODEL) ?: BottomSheetModel()
    }

    override fun title(): String {
        super.title()
        return ""
    }

    interface BottomSheetListener {
        fun onBottomSheetButtonClicked()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY)
        parentView?.post {
            parentView.measure(widthSpec, 0)
            updateHeight(parentView.measuredHeight)
        }
    }

    data class BottomSheetModel(
            val title: String = "",
            val desc: String = "",
            val imageUrl: String = "",
            val btnTitle: String = "",
            val trackingFlag: String = ""
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString() ?: "",
                source.readString() ?: "",
                source.readString() ?: "",
                source.readString() ?: "",
                source.readString() ?:""
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(title)
            writeString(desc)
            writeString(imageUrl)
            writeString(btnTitle)
            writeString(trackingFlag)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<BottomSheetModel> = object : Parcelable.Creator<BottomSheetModel> {
                override fun createFromParcel(source: Parcel): BottomSheetModel = BottomSheetModel(source)
                override fun newArray(size: Int): Array<BottomSheetModel?> = arrayOfNulls(size)
            }
        }
    }
}