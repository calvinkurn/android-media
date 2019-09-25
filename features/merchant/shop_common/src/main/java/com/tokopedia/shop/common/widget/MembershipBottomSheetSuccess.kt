package com.tokopedia.shop.common.widget

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter

class MembershipBottomSheetSuccess : BottomSheets() {

    private var titleBsMembership: String = ""
    private var descTitleMembership: String = ""
    private var resultCode: String = ""
    private var questId: Int = 0

    lateinit var txtTitle: TextView
    lateinit var txtDesc: TextView
    lateinit var btnClaim: Button
    lateinit var imgBsMembership: ImageView

    private var listener: MembershipStampAdapter.MembershipStampAdapterListener? = null

    companion object {

        private const val IMG_BS_MEMBERSHIP_SUCCESS = "https://ecs7.tokopedia.net/img/android/membership/coupon_success.png"
        private const val IMG_BS_MEMBERSHIP_FAIL = "https://ecs7.tokopedia.net/img/android/membership/coupon_fail.png"
        private const val TITLE_PARAM = "title_membership"
        private const val DESC_PARAM = "desc_membership"
        private const val CODE_PARAM = "code_membership"
        private const val QUEST_ID_PARAM = "quest_id_membership"

        private const val CODE_SUCCESS = "200"

        @JvmStatic
        fun newInstance(title: String, desc: String, resultCode: String, lastQuestId: Int): MembershipBottomSheetSuccess {
            return MembershipBottomSheetSuccess().apply {
                val bundle = Bundle()
                bundle.putString(TITLE_PARAM, title)
                bundle.putString(DESC_PARAM, desc)
                bundle.putString(CODE_PARAM, resultCode)
                bundle.putInt(QUEST_ID_PARAM, lastQuestId)
                arguments = bundle
            }
        }
    }

    private fun initVar() {
        titleBsMembership = arguments?.getString(TITLE_PARAM) ?: ""
        descTitleMembership = arguments?.getString(DESC_PARAM) ?: ""
        resultCode = arguments?.getString(CODE_PARAM) ?: ""
        questId = arguments?.getInt(QUEST_ID_PARAM) ?: 0
    }

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheet_membership_success

    override fun getTheme(): Int = R.style.BaseBottomSheetDialogRounded

    override fun getBaseLayoutResourceId(): Int = R.layout.base_bottom_sheets_rounded

    override fun title(): String = ""

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    fun setListener(listener: MembershipStampAdapter.MembershipStampAdapterListener) {
        this.listener = listener
    }

    override fun initView(view: View) {
        initVar()
        txtTitle = view.findViewById(R.id.title_bs_membership)
        txtDesc = view.findViewById(R.id.desc_bs_membership)
        btnClaim = view.findViewById(R.id.btn_check_my_coupon)
        imgBsMembership = view.findViewById(R.id.img_membership_success)

        if (resultCode != CODE_SUCCESS) {
            ImageHandler.LoadImage(view.findViewById(R.id.img_membership_success), IMG_BS_MEMBERSHIP_FAIL)
            btnClaim.text = context?.getString(R.string.title_try_again)
            btnClaim.setOnClickListener {
                listener?.onButtonClaimClicked(questId)
                dismiss()
            }
        } else {
            //route to voucher
            ImageHandler.LoadImage(view.findViewById(R.id.img_membership_success), IMG_BS_MEMBERSHIP_SUCCESS)
            btnClaim.text = context?.getString(R.string.bs_button_txt)
            btnClaim.setOnClickListener {
                listener?.goToVoucherOrRegister()
            }
        }

        txtTitle.text = titleBsMembership
        txtDesc.text = descTitleMembership

    }

}