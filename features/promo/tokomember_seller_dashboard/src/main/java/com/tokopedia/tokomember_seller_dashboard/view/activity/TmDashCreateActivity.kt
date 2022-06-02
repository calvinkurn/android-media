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
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_SINGLE
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PROGRAM
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CREATE_SCREEN_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_PRIMARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_CTA_SECONDARY
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TM_DIALOG_CANCEL_TITLE
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmDashPreviewFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmMultipleCuponCreateFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmProgramFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmSingleCouponCreateFragment

class TmDashCreateActivity : AppCompatActivity(), TmOpenFragmentCallback {

    private var screenType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tm_dash_create_activity)

        intent.extras?.getInt(BUNDLE_CREATE_SCREEN_TYPE, CARD)?.let {
            screenType = it
        }

        when(screenType){
            CARD ->{
                intent.extras?.let { TmCreateCardFragment.newInstance(it) }?.let { addFragment(it, TmCreateCardFragment.TAG_CARD_CREATE) }
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
                    setTitle(TM_DIALOG_CANCEL_TITLE)
                    setDescription(TM_DIALOG_CANCEL_DESC)
                    setPrimaryCTAText(TM_DIALOG_CANCEL_CTA_PRIMARY)
                    setSecondaryCTAText(TM_DIALOG_CANCEL_CTA_SECONDARY)
                    setPrimaryCTAClickListener {
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        dismiss()
                        finish()
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

        private var tmCouponListRefreshCallback: TmCouponListRefreshCallback? = null

        fun openActivity(
            shopId: Int,
            activity: Activity?,
            screenType: Int,
            programActionType: Int = ProgramActionType.CREATE,
            requestCode: Int?,
            programId: Int
        ){
            activity?.let {
                val intent = Intent(it, TmDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_CREATE_SCREEN_TYPE, screenType)
                intent.putExtra(BUNDLE_PROGRAM_TYPE, programActionType)
                intent.putExtra(BUNDLE_PROGRAM_ID, programId)
                requestCode?.let {
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, intent.extras)
                } ?:  it.startActivity(intent)
            }
        }

        fun openActivity(
            activity: Activity?,
            screenType: Int,
            voucherId: Int,
            tmCouponListRefreshCallback: TmCouponListRefreshCallback
        ){
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            activity?.let {
                val intent = Intent(it, TmDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_CREATE_SCREEN_TYPE, screenType)
                intent.putExtra(BUNDLE_VOUCHER_ID, voucherId)
                intent.putExtra(ACTION_EDIT, true)
                it.startActivity(intent)
            }
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
            COUPON_SINGLE ->{}
            COUPON_MULTIPLE ->{
                intent.extras?.let { TmMultipleCuponCreateFragment.newInstance(it) }?.let { addFragment(it, "") }
            }
            PREVIEW ->{
                bundle.let { TmDashPreviewFragment.newInstance(it) }.let { addFragment(it, "") }
            }
        }

    }
}