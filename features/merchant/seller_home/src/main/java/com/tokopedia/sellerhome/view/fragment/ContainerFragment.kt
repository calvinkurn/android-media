package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SomTabConst
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.model.NotificationChatUiModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_sah_container.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class ContainerFragment : Fragment() {

    companion object {
        fun newInstance(): ContainerFragment {
            return ContainerFragment()
        }
    }

    private val sellerHomeRouter: SellerHomeRouter? by lazy {
        val applicationContext = activity?.applicationContext
        return@lazy if (applicationContext is SellerHomeRouter)
            applicationContext
        else
            null
    }
    private val homeFragment by lazy { SellerHomeFragment.newInstance() }
    private var somListFragment: Fragment? = null
    private var currentFragment: Fragment? = null
    private val fragmentManger: FragmentManager by lazy { childFragmentManager }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val sharedViewModel: SharedViewModel? by lazy {
        return@lazy if (null != activity) {
            ViewModelProvider(activity!!, viewModelFactory).get(SharedViewModel::class.java)
        } else
            null
    }

    private var hasAttachHomeFragment = false
    private var hasAttachSomFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sah_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeCurrentSelectedMenu()
        observeToolbarTitle()
        setupDefaultPage()
    }

    private fun setupView() = view?.run {
        sahToolbar.setOnNotificationClickListener {
            sharedViewModel?.setCurrentSelectedPage(PageFragment(FragmentType.CHAT))
        }
    }

    private fun setupDefaultPage() {
        currentFragment = homeFragment
        sharedViewModel?.setToolbarTitle(context?.getString(R.string.sah_home).orEmpty())
        sharedViewModel?.setCurrentSelectedPage(PageFragment(FragmentType.HOME))
    }

    private fun observeCurrentSelectedMenu() {
        sharedViewModel?.currentSelectedPage?.observe(this, Observer { page ->
            when (page.type) {
                FragmentType.HOME -> {
                    if (!hasAttachHomeFragment) {
                        addFragment(homeFragment)
                        hasAttachHomeFragment = true
                    }
                    showFragment(homeFragment, getString(R.string.sah_home))
                }
                FragmentType.PRODUCT -> {
                    if (!hasAttachHomeFragment) {
                        addFragment(homeFragment)
                        hasAttachHomeFragment = true
                    }
                    showFragment(homeFragment, getString(R.string.sah_product))
                }
                FragmentType.CHAT -> {
                    if (!hasAttachHomeFragment) {
                        addFragment(homeFragment)
                        hasAttachHomeFragment = true
                    }
                    showFragment(homeFragment, getString(R.string.sah_chat))
                }
                FragmentType.ORDER -> showSomPageFragment(page)
            }
        })
    }

    private fun showSomPageFragment(page: PageFragment) {
        if (null == somListFragment || (page.tabPage.isNotBlank() && SomTabConst.STATUS_ALL_ORDER != page.tabPage)) {
            somListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
            hasAttachSomFragment = false
        }

        somListFragment?.let {
            if (!hasAttachSomFragment) {
                addFragment(it)
                hasAttachSomFragment = true
            }
            showFragment(it, getString(R.string.sah_sale))
        }
    }

    fun showChatNotificationBadge(chat: NotificationChatUiModel) {
        if (chat.unreads > 0)
            view?.sahToolbar?.showBadge()
        else
            view?.sahToolbar?.removeBadge()
    }

    private fun <T : Fragment> addFragment(fragment: T) {
        fragmentManger.beginTransaction()
                .add(R.id.sahFragmentContainer, fragment, fragment.tag)
                .hide(fragment)
                .commit()
    }

    private fun showFragment(fragment: Fragment, title: String) {
        currentFragment?.let {
            fragmentManger.beginTransaction()
                    .hide(it)
                    .show(fragment)
                    .commit()
            currentFragment = fragment
        }
        view?.sahToolbar?.title = title
    }

    private fun observeToolbarTitle() {
        sharedViewModel?.toolbarTitle?.observe(viewLifecycleOwner, Observer {
            view?.sahToolbar?.title = it
        })
    }
}