package com.tokopedia.tokomember

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.user.session.UserSession
import timber.log.Timber

class TokomemberActivity : BaseActivity() , TokomemberBottomSheetView.OnFinishedListener {
    private var isOnResume = false
    lateinit var userSession: UserSession

    companion object {
        const val KEY_MEMBERSHIP = "key_membership"
        fun getIntent(context: Context, bottomSheetContentItem: BottomSheetContentItem?): Intent {
            val intent = Intent(context, TokomemberActivity::class.java)
            intent.putExtra(KEY_MEMBERSHIP,bottomSheetContentItem)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        handleDimming()
        showTmBottomSheetDetail()
    }

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun showTmBottomSheetDetail() {
        val bottomSheet = TokomemberBottomSheetView.newInstance(intent?.extras?:Bundle())
        bottomSheet.setOnFinishedListener(this)
        bottomSheet.setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheet.bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }
        bottomSheet.setOnDismissListener {
            if (isOnResume) {
                finish()
            }
        }
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
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

    override fun onFinish() {
        finish()
    }

}