package com.tokopedia.inbox.universalinbox.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.iconnotification.IconNotification
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inbox.databinding.UniversalInboxActivityBinding
import com.tokopedia.inbox.universalinbox.di.UniversalInboxActivityComponentFactory
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.ICON_PERCENTAGE_X_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.ICON_PERCENTAGE_Y_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil.getStringCounter
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx

class UniversalInboxActivity: BaseSimpleActivity(), HasComponent<UniversalInboxComponent> {

    private var universalInboxComponent: UniversalInboxComponent? = null
    private var viewBinding: UniversalInboxActivityBinding? = null

    override fun getNewFragment(): Fragment {
        return UniversalInboxFragment.getFragment(
            supportFragmentManager,
            classLoader,
            getFragmentBundle()
        )
    }

    private fun getFragmentBundle(): Bundle { //TODO: if any param, put here
        return Bundle()
    }

    override fun getComponent(): UniversalInboxComponent {
        return universalInboxComponent?: initializeUniversalInboxComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupToolbar()
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = UniversalInboxFragmentFactory()
    }

    private fun setupViewBinding() {
        viewBinding = UniversalInboxActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    override fun getParentViewResourceID(): Int {
        return R.id.inbox_layout_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.universal_inbox_activity
    }

    private fun setupToolbar() {
        val mInflater = LayoutInflater.from(this)
        val toolbarCustomView = mInflater.inflate(R.layout.universal_inbox_toolbar, null)
        toolbarCustomView.findViewById<IconNotification>(R.id.inbox_icon_notif).apply {
            val strCounter = getStringCounter(100)
            notificationRef.showWithCondition(strCounter.isNotEmpty())
            setNotifXY(
                ICON_PERCENTAGE_X_POSITION,
                ICON_PERCENTAGE_Y_POSITION
            )
            notificationRef.setNotification(
                notif = strCounter,
                notificationType = NotificationUnify.COUNTER_TYPE,
                colorType = NotificationUnify.COLOR_PRIMARY
            )
            setImageWithUnifyIcon(IconUnify.BELL)
            notificationGravity = Gravity.TOP or Gravity.END
        }
        viewBinding?.inboxToolbar?.apply {
            isShowShadow = true
            customView(toolbarCustomView)
            headerWrapperView.setPadding(0, 0, 0, 0)
            textWrapperView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToStart = ConstraintLayout.LayoutParams.UNSET
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            setSupportActionBar(this)
        }
    }

    private fun initializeUniversalInboxComponent(): UniversalInboxComponent {
        return UniversalInboxActivityComponentFactory
            .instance
            .createUniversalInboxComponent(application).also {
                universalInboxComponent = it
            }
    }
}
