package com.tokopedia.navigation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread
import com.tokopedia.navigation.presentation.adapter.NotificationFragmentAdapter
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.fragment.NotificationFragment
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.navigation.presentation.presenter.NotificationActivityPresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import javax.inject.Inject

/**
 * Created by meta on 20/06/18.
 */
@DeepLink(ApplinkConst.NOTIFICATION)
class NotificationActivity : BaseTabActivity(), HasComponent<BaseAppComponent>, NotificationActivityContract.View {

    @Inject
    lateinit var presenter: NotificationActivityPresenter

    private var fragmentAdapter: NotificationFragmentAdapter? = null
    private val tabList = ArrayList<NotificationFragmentAdapter.NotificationFragmentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem("Transaksi", NotificationFragment()))
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem("Update", NotificationUpdateFragment()))
        super.onCreate(savedInstanceState)
        initInjector()
        initView()
    }

    fun initInjector() {
        DaggerNotificationUpdateComponent.builder()
                .baseAppComponent(component).build().inject(this)
        presenter.attachView(this)
    }

    private fun initView() {
        initTabLayout()
        presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
    }

    private fun onSuccessGetTotalUnreadCounter(): (NotificationUpdateTotalUnread) -> Unit {
        return {
            var defaultTitle = getString(R.string.title_update_notification)
            var counter: String
            if (it.pojo.notifUnreadInt > 0) {
                counter = getString(R.string.title_counter_update_notification, it.pojo.notifUnreadString)
                var titleView: TextView? = tabLayout.getTabAt(1)?.customView?.findViewById(R.id.title)
                titleView?.let {
                    it.text = String.format("%s %s", defaultTitle, counter)
                }
            }
        }
    }
    private fun onSuccessGetUpdateUnreadCounter(): (NotificationUpdateUnread) -> Unit {
        return {
            if (it.pojo.notifUnreadInt > 0) {
                var notif = tabLayout.getTabAt(1)?.customView?.findViewById<View>(R.id.circle)
                notif?.visibility = View.VISIBLE
            }
        }
    }

    private fun initTabLayout() {
        for (i in 0 until tabList.size) {
            tabLayout.addTab(tabLayout.newTab())
        }
        for (i in 0 until tabLayout.tabCount) {
            val title = tabList[i].title
            val tab = tabLayout.getTabAt(i)
            tab?.customView = createCustomView(title)
            if (i == viewPager.currentItem) {
                setTabSelectedView(tab?.customView)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.elevation = 0f
        }
        tabLayout.setBackgroundResource(R.color.white)
        tabLayout.tabMode = TabLayout.MODE_FIXED

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabUnSelectedView(tab.customView)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position, true)
                setTabSelectedView(tab.customView)
                resetCircle(tab.customView)
                presenter.clearNotifCounter()
            }
        })
    }

    private fun resetCircle(customView: View?) {
        var notif = customView?.findViewById<View>(R.id.circle)
        notif?.visibility = View.GONE
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.label_green))
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.label_grey))
    }

    private fun createCustomView(title: String): View? {
        var customView = LayoutInflater.from(this).inflate(R.layout.item_notification_tab_title, null);
        var titleView = customView.findViewById<TextView>(R.id.title)
        titleView.text = title
        return customView
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = NotificationFragmentAdapter(supportFragmentManager)
        fragmentAdapter?.setItemList(tabList)

        return fragmentAdapter
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_notification_page
    }

    override fun getPageLimit(): Int {
        return tabList.size + 1
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun updateTotalUnreadCounter() : () -> Unit {
        return {presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())}
    }

    companion object {

        fun start(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }
    }
}
