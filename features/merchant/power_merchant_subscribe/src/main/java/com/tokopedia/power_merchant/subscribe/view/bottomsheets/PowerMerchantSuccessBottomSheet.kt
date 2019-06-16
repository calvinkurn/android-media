package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.power_merchant.subscribe.R

class PowerMerchantSuccessBottomSheet : BottomSheets() {

    private lateinit var buttonSubmit: Button
    private lateinit var imgSuccessPm: ImageView
    private lateinit var txtSuccessHeaderBs: TextViewCompat
    private lateinit var txtSuccessDescBs: TextViewCompat
    private lateinit var model: BottomSheetModel
    private var listener: BottomSheetListener? = null

    companion object {
        private const val MODEL = "model"

        @JvmStatic
        fun newInstance(model: BottomSheetModel): PowerMerchantSuccessBottomSheet {
            return PowerMerchantSuccessBottomSheet().apply {
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
        return R.layout.bottom_sheets_pm_success
    }

    override fun initView(view: View) {
        initVar()
        imgSuccessPm = view.findViewById(R.id.img_btm_sheets)
        buttonSubmit = view.findViewById(R.id.button_checknow)
        txtSuccessHeaderBs = view.findViewById(R.id.txt_success_header_bs)
        txtSuccessDescBs = view.findViewById(R.id.txt_success_desc_bs)

        imgSuccessPm.loadImage(model.imageUrl)
        txtSuccessHeaderBs.text = MethodChecker.fromHtml(model.title)
        txtSuccessDescBs.text = MethodChecker.fromHtml(model.desc)

        buttonSubmit.text = model.btnTitle
        buttonSubmit.setOnClickListener {
            listener?.onButtonClicked()
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
        fun onButtonClicked()
    }

    data class BottomSheetModel(
            val title: String = "",
            val desc: String = "",
            val imageUrl: String = "",
            val btnTitle: String = ""
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString() ?: "",
                source.readString() ?: "",
                source.readString() ?: "",
                source.readString() ?: ""
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(title)
            writeString(desc)
            writeString(imageUrl)
            writeString(btnTitle)
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