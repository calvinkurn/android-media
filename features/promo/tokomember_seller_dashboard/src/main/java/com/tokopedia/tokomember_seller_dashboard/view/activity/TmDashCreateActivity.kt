package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.CARD
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE_BUAT
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE_EXTEND
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_SINGLE
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW_BUAT
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW_EXTEND
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PROGRAM
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.EditCardCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CREATE_SCREEN_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPEN_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.TM_CARD_DIALOG_CTA_PRIMARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_CARD_DIALOG_CTA_SECONDARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_CARD_DIALOG_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TM_CARD_DIALOG_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_PRIMARY_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_PRIMARY_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_SECONDARY_COUPON
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_SECONDARY_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_SECONDARY_EXTEND_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_SECONDARY_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC_COUPON
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC_COUPON_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC_EXTEND_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE_COUPON
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE_COUPON_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE_EXTEND_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmDashPreviewFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmMultipleCuponCreateFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmProgramFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmSingleCouponCreateFragment

class TmDashCreateActivity : AppCompatActivity(), TmOpenFragmentCallback {

    private var screenType: Int = 0
    private var tmTracker : TmTracker ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tm_dash_create_activity)
        tmTracker = TmTracker()
        intent.extras?.getInt(BUNDLE_CREATE_SCREEN_TYPE, CARD)?.let {
            screenType = it
        }

        when(screenType){
            CARD ->{
                intent.extras?.let {
                    TmCreateCardFragment.newInstance(it)
                }?.let {
                    TmDashCreateActivity.editCardCallback?.let { it1 -> it.setCardEditCallback(it1) }
                    addFragment(it, TmCreateCardFragment.TAG_CARD_CREATE)
                }
            }
            PROGRAM ->{
                intent.extras?.let { TmProgramFragment.newInstance(it) }?.let  { addFragment(it, "") }
            }
            COUPON_SINGLE ->{
                intent.extras?.let { tmCouponListRefreshCallback?.let { it1 ->
                    TmSingleCouponCreateFragment.newInstance(it,
                        it1
                    )
                } }?.let { addFragment(it, "") }
            }
            COUPON_MULTIPLE ->{
                intent.extras?.let { TmMultipleCuponCreateFragment.newInstance(it) }?.let { addFragment(it, "") }
            }
            PREVIEW ->{
                intent.extras?.let { TmDashPreviewFragment.newInstance(it) }?.let { addFragment(it, "") }
            }
        }
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 1 -> {
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager.backStackEntryCount == 1 -> {
                val dialogCancel =
                    DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
                dialogCancel.apply {
                    when (screenType) {
                        PROGRAM -> {
                            val shopId =
                                supportFragmentManager.fragments.firstOrNull()?.arguments?.getInt(
                                    BUNDLE_SHOP_ID
                                ).toString()
                            if (supportFragmentManager.fragments.firstOrNull() is TmProgramFragment) {
                                setTitle(TM_DIALOG_CANCEL_TITLE_PROGRAM)
                                setDescription(TM_DIALOG_CANCEL_DESC_PROGRAM)
                                setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_PROGRAM)
                                setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_PROGRAM)
                            }
                            if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EDIT){
                                setTitle(TM_DIALOG_CANCEL_TITLE_EDIT_PROGRAM)
                                setDescription(TM_DIALOG_CANCEL_DESC_EDIT_PROGRAM)
                                setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_EDIT_PROGRAM)
                                setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_EDIT_PROGRAM)
                            }
                            if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EXTEND){
                                setTitle(TM_DIALOG_CANCEL_TITLE_EXTEND_PROGRAM)
                                setDescription(TM_DIALOG_CANCEL_DESC_EXTEND_PROGRAM)
                                setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_PROGRAM)
                                setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_EXTEND_PROGRAM)
                            }
                            setPrimaryCTAClickListener {
                                if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EXTEND) {
                                    tmTracker?.clickProgramExtensionPopUpPrimary(shopId, intent?.extras?.getInt(BUNDLE_PROGRAM_ID).toString())
                                }
                                else if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EDIT) {
                                    tmTracker?.clickProgramEditPopUpPrimary(shopId, intent?.extras?.getInt(BUNDLE_PROGRAM_ID).toString())
                                }
                                else {
                                    tmTracker?.clickProgramCreationCancelPopupPrimary(shopId)
                                }
                                dismiss()
                            }
                            setSecondaryCTAClickListener {
                                if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EXTEND) {
                                    tmTracker?.clickProgramExtensionPopUpSecondary(shopId, intent?.extras?.getInt(BUNDLE_PROGRAM_ID).toString())
                                }
                                else if(intent.extras?.getInt(BUNDLE_PROGRAM_TYPE) == ProgramActionType.EDIT) {
                                    tmTracker?.clickProgramEditPopUpSecondary(shopId, intent?.extras?.getInt(BUNDLE_PROGRAM_ID).toString())
                                }
                                else {
                                    tmTracker?.clickProgramCreationCancelPopupSecondary(shopId)
                                }
                                dismiss()
                                finish()
                            }
                        }
                        COUPON_SINGLE -> {
                            setTitle(TM_DIALOG_CANCEL_TITLE_COUPON)
                            setDescription(TM_DIALOG_CANCEL_DESC_COUPON)
                            setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON)
                            setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_COUPON)
                            if(intent.extras?.getBoolean(ACTION_EDIT) == true){
                                setTitle(TM_DIALOG_CANCEL_TITLE_COUPON_EDIT)
                                setDescription(TM_DIALOG_CANCEL_DESC_COUPON_EDIT)
                                setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON_EDIT)
                                setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_COUPON)
                            }
                            val arg = supportFragmentManager.fragments.firstOrNull()?.arguments
                            val shopId  = arg?.getInt(BUNDLE_SHOP_ID)
                            setPrimaryCTAClickListener {
                                tmTracker?.clickCouponCancelPopUpPrimary(shopId.toString())
                                dismiss()
                            }
                            setSecondaryCTAClickListener {
                                tmTracker?.clickCouponCancelPopUpSecondary(shopId.toString())
                                dismiss()
                                finish()
                            }
                        }
                        COUPON_MULTIPLE -> {
                            setTitle(TM_DIALOG_CANCEL_TITLE_COUPON)
                            setDescription(TM_DIALOG_CANCEL_DESC_COUPON)
                            setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON)
                            setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_COUPON)
                            setPrimaryCTAClickListener {
                                dismiss()
                            }
                            setSecondaryCTAClickListener {
                                dismiss()
                                finish()
                            }
                        }
                        COUPON_MULTIPLE_BUAT -> {
                            setTitle(TM_DIALOG_CANCEL_TITLE_COUPON)
                            setDescription(TM_DIALOG_CANCEL_DESC_COUPON)
                            setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY_COUPON)
                            setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY_COUPON)
                            setPrimaryCTAClickListener {
                                dismiss()
                            }
                            setSecondaryCTAClickListener {
                                dismiss()
                                finish()
                            }
                        }
                        CARD -> {
                            if( supportFragmentManager.fragments.firstOrNull() is TmCreateCardFragment){
                                val arg = supportFragmentManager.fragments.firstOrNull()?.arguments
                                val shopId  = arg?.getInt(BUNDLE_SHOP_ID)
                                val shopName = arg?.getString(BUNDLE_SHOP_NAME)
                                val shopAvatar = arg?.getString(BUNDLE_SHOP_AVATAR)
                                val openBS = arg?.getBoolean(BUNDLE_OPEN_BS)

                                setTitle(TM_CARD_DIALOG_TITLE)
                                setDescription(TM_CARD_DIALOG_DESC)
                                setPrimaryCTAText(TM_CARD_DIALOG_CTA_PRIMARY)
                                setSecondaryCTAText(TM_CARD_DIALOG_CTA_SECONDARY)
                                setPrimaryCTAClickListener {
                                    tmTracker?.clickCardCancelPrimary(shopId.toString())
                                    dismiss()
                                }
                                setSecondaryCTAClickListener {
                                    tmTracker?.clickCardCancelSecondary(shopId.toString())
                                    dismiss()
                                    finish()
                                    TokomemberDashIntroActivity.openActivity(shopId?:0,shopAvatar?:"",shopName?:"",openBS?:false,this.context)
                                }
                            }
                        }
                    }
                }
                dialogCancel.show()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_view, fragment, tag)
            .addToBackStack("replace " + fragment::class.java.simpleName).commit()
    }

    companion object{

        private var editCardCallback: EditCardCallback? = null
        private var tmCouponListRefreshCallback: TmCouponListRefreshCallback? = null

        fun openActivity(
            shopId: Int,
            activity: Activity?,
            screenType: Int,
            programActionType: Int = ProgramActionType.CREATE,
            requestCode: Int?,
            programId: Int?,
            cardId: Int = 0,
            shopAvatar: String = ""
        ){
            activity?.let {
                val intent = Intent(it, TmDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_CREATE_SCREEN_TYPE, screenType)
                intent.putExtra(BUNDLE_PROGRAM_TYPE, programActionType)
                intent.putExtra(BUNDLE_PROGRAM_ID, programId)
                intent.putExtra(BUNDLE_CARD_ID, cardId)
                intent.putExtra(BUNDLE_SHOP_AVATAR, shopAvatar)
                requestCode?.let {
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, intent.extras)
                } ?:  it.startActivity(intent)
            }
        }

        fun openActivity(
            activity: Activity?,
            screenType: Int,
            voucherId: Int?,
            tmCouponListRefreshCallback: TmCouponListRefreshCallback?,
            edit: Boolean = false,
            duplicate: Boolean = false
        ){
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            activity?.let {
                val intent = Intent(it, TmDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_CREATE_SCREEN_TYPE, screenType)
                intent.putExtra(BUNDLE_VOUCHER_ID, voucherId)
                intent.putExtra(ACTION_EDIT, edit)
                intent.putExtra(ACTION_DUPLICATE, duplicate)
                it.startActivity(intent)
            }
        }

        fun setCardEditCallback(editCardCallback: EditCardCallback){
            this.editCardCallback = editCardCallback
        }
    }

    override fun openFragment(screenType: Int, bundle: Bundle) {

        when(screenType){
            CARD ->{
                bundle.let { TmCreateCardFragment.newInstance(it) }.let { addFragment(it, TmCreateCardFragment.TAG_CARD_CREATE) }
            }
            PROGRAM ->{
                bundle.let { TmProgramFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            COUPON_SINGLE ->{
                bundle.let { tmCouponListRefreshCallback?.let { it1 ->
                    TmSingleCouponCreateFragment.newInstance(it,
                        it1
                    )
                } }?.let { addFragment(it, "") }
            }
            COUPON_MULTIPLE ->{
                bundle.let { TmMultipleCuponCreateFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            COUPON_MULTIPLE_EXTEND ->{
                bundle.putInt(BUNDLE_CREATE_SCREEN_TYPE, COUPON_MULTIPLE_EXTEND)
                bundle.let { TmMultipleCuponCreateFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            COUPON_MULTIPLE_BUAT ->{
                bundle.putInt(BUNDLE_CREATE_SCREEN_TYPE, COUPON_MULTIPLE_BUAT)
                bundle.let { TmMultipleCuponCreateFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            PREVIEW ->{
                bundle.let { TmDashPreviewFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            PREVIEW_BUAT->{
                bundle.putInt(BUNDLE_CREATE_SCREEN_TYPE, PREVIEW_BUAT)
                bundle.let { TmDashPreviewFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            PREVIEW_EXTEND->{
                bundle.putInt(BUNDLE_CREATE_SCREEN_TYPE, PREVIEW_EXTEND)
                bundle.let { TmDashPreviewFragment.newInstance(it) }.let { addFragment(it, "") }
            }
        }

    }

}
