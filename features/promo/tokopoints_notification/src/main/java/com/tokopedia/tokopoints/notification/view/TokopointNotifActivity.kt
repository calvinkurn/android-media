package com.tokopedia.tokopoints.notification.view
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.tokopoints.notification.di.DaggerTokopointNotifComponent
import com.tokopedia.tokopoints.notification.model.BottomSheetModel
import com.tokopedia.tokopoints.notification.model.PopupNotification
import timber.log.Timber

class TokopointNotifActivity : BaseActivity()  {
    private var isOnResume = false
    private var popupNotification: PopupNotification?=null

    companion object {
        const val KEY_POPUP = "key_popup"
        fun getIntent(context: Context, popupNotification: PopupNotification?): Intent {
            val intent = Intent(context, TokopointNotifActivity::class.java)
            intent.putExtra(KEY_POPUP,popupNotification)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        handleDimming()
        showTmBottomSheetDetail()
    }

    private fun initDagger(){
        DaggerTokopointNotifComponent.builder()
            .build().inject(this)
    }

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun showTmBottomSheetDetail() {
        popupNotification = intent?.getParcelableExtra(KEY_POPUP)
        val bundle = Bundle()
        bundle.putParcelable("bsm",mapDataToBottomsheet())
        val tokoPointsPopupNotificationBottomSheet = RewardCommonBottomSheet.newInstance(bundle)

        tokoPointsPopupNotificationBottomSheet.setOnDismissListener {
            if (isOnResume) {
                this.finish()
            }
        }

        tokoPointsPopupNotificationBottomSheet.show(supportFragmentManager, "BottomSheet Tag")
    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
    }

    override fun onStop() {
        super.onStop()
        isOnResume = false
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isOnResume = false
    }

    override fun onBackPressed() {
         this.finish()
    }

    private fun mapDataToBottomsheet(): BottomSheetModel{
        return BottomSheetModel(
            bottomSheetTitle = popupNotification?.titleHeader ,
            contentTitle = popupNotification?.title,
            contentDescription = popupNotification?.text,
            buttonText = popupNotification?.buttonText,
            applink = popupNotification?.appLink?:"",
            imageUrl = popupNotification?.imageURL
        )
    }
}