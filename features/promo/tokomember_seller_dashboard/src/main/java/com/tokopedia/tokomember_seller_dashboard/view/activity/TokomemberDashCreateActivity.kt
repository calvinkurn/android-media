package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.CARD
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.COUPON
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.PREVIEW
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.PROGRAM
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmMultipleCuponCreateFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmSingleCouponCreateFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashPreviewFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberProgramFragment

class TokomemberDashCreateActivity : AppCompatActivity(), TmOpenFragmentCallback {

//    override fun getNewFragment(): Fragment {
//        return TokomemberCreateCardFragment.newInstance(intent.extras ?: Bundle())
//    }

    private var screenType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tm_dash_create_activity)

        intent.extras?.getInt(BUNDLE_PROGRAM, CARD)?.let {
            screenType = it
        }

        supportFragmentManager.addOnBackStackChangedListener {
            Log.d("backstack", "onCreate: " + supportFragmentManager.getBackStackEntryAt(0).name)
        }

        when(screenType){
            CARD ->{
                intent.extras?.let { TokomemberCreateCardFragment.newInstance(it) }?.let { addFragment(it, TokomemberCreateCardFragment.TAG_CARD_CREATE) }
            }
            PROGRAM ->{
                intent.extras?.let { TokomemberProgramFragment.newInstance(it) }?.let  { addFragment(it, "") }
            }
            COUPON ->{
                intent.extras?.let { TmSingleCouponCreateFragment.newInstance(it) }?.let { addFragment(it, "") }
            }
            PREVIEW ->{
                intent.extras?.let { TokomemberDashPreviewFragment.newInstance(it) }?.let { addFragment(it, "") }
            }
        }
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 1 -> {
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager.backStackEntryCount == 1 -> {
                if ( supportFragmentManager.fragments[0] is TokomemberCreateCardFragment){
                    //todo error ui and dialog
                }
                val dialogCancel =
                    DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
                dialogCancel.apply {
                    setTitle("Yakin batalkan program?")
                    setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pembuatan TokoMember, lho.")
                    setPrimaryCTAText("Lanjut")
                    setSecondaryCTAText("Batalkan Program")
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

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_view, fragment, tag)
            .addToBackStack("replace " + fragment::class.java.simpleName).commit()
    }

    companion object{
        fun openActivity(
            shopId: Int,
            activity: Activity?,
            screenType: Int,
            programActionType: Int = ProgramActionType.CREATE,
            requestCode: Int?,
            programId: Int
        ){
            activity?.let {
                val intent = Intent(it, TokomemberDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_PROGRAM, screenType)
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
        ){
            activity?.let {
                val intent = Intent(it, TokomemberDashCreateActivity::class.java)
                intent.putExtra(BUNDLE_PROGRAM, screenType)
                intent.putExtra(BUNDLE_VOUCHER_ID, voucherId)
                intent.putExtra(ACTION_EDIT, true)
                it.startActivity(intent)
            }
        }
    }

    override fun openFragment(screenType: Int, bundle: Bundle) {

        when(screenType){
            CARD ->{
                bundle.let { TokomemberCreateCardFragment.newInstance(it) }.let { addFragment(it, TokomemberCreateCardFragment.TAG_CARD_CREATE) }
            }
            PROGRAM ->{
                bundle.let { TokomemberProgramFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            COUPON ->{
                bundle.let { TmMultipleCuponCreateFragment.newInstance(it) }.let { addFragment(it, "") }
            }
            PREVIEW ->{
                bundle.let { TokomemberDashPreviewFragment.newInstance(it) }.let { addFragment(it, "") }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_REFRESH){
            if(resultCode == Activity.RESULT_OK){
            }
        }
    }
//    DashHomeActivity -> TokoDashHomeMainFragment -> Program List Fragment -> Create Activity -> Program Fragment
}