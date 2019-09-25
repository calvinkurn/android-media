package com.tokopedia.officialstore.presentation

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.presentation.adapter.OfficialHomeContainerAdapter
import com.tokopedia.officialstore.presentation.di.DaggerOfficialHomeComponent
import com.tokopedia.officialstore.presentation.model.Category
import com.tokopedia.searchbar.MainToolbar
import javax.inject.Inject

class OfficialHomeContainerFragment : BaseDaggerFragment(),
        OfficialHomeContainerView,
        AllNotificationListener,
        RecyclerViewScrollListener {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeContainerFragment().apply { arguments = bundle }
    }

    @Inject lateinit var presenter: OfficialHomeContainerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_official_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0

    private var categoryList: MutableList<Category> = mutableListOf()

    private val tabAdapter: OfficialHomeContainerAdapter by lazy {
        OfficialHomeContainerAdapter(childFragmentManager, categoryList)
    }

    private fun init(view: View) {
        configStatusBar(view)
        configMainToolbar(view)

        tabLayout = view.findViewById(R.id.tablayout)
        viewPager = view.findViewById(R.id.viewpager)

        dummy() // TODO implement api

        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)

        dummyIcons() // TODO set icons from categoryList
    }

    private fun dummy() {
        categoryList.add(Category("1", "Home", "", "os-home"))
        categoryList.add(Category("2", "Fashion", "", "fasion-os-home"))
        categoryList.add(Category("3", "Groceries", "", "groceries-os-home"))
        categoryList.add(Category("4", "Electronic", "", "electronic-os-home"))
    }

    private fun dummyIcons() {
        for (i in 0 until categoryList.size) {
            tabLayout?.getTabAt(i)?.setIcon(R.drawable.dummy)
        }
    }

    override fun onScrollDown() {
        // visible icon tab layout
    }

    override fun onScrollUp() {
        // gone icon tab layout
    }

    //status bar background compability
    private fun configStatusBar(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        activity?.let {
            statusBar?.layoutParams?.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        statusBar?.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun configMainToolbar(view: View) {
        mainToolbar = view.findViewById(R.id.maintoolbar)
        mainToolbar?.setQuerySearch(getString(R.string.os_query_search))
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox) // notify badge after toolbar created
    }

    // from: GlobalNav, to show notification maintoolbar
    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        mainToolbar?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
        }
        badgeNumberNotification = notificationCount
        badgeNumberInbox = inboxCount
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerOfficialHomeComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication)
                        .baseAppComponent)
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}