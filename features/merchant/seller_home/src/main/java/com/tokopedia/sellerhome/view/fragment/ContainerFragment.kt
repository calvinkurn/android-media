package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import kotlinx.android.synthetic.main.fragment_sah_container.view.*

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class ContainerFragment : Fragment() {

    companion object {
        const val FRAGMENT_HOME = "home"
        const val FRAGMENT_PRODUCT = "produk"
        const val FRAGMENT_ORDER = "som"
        const val FRAGMENT_OTHER = "lainnya"

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
    private val homeFragment: SellerHomeFragment by lazy { SellerHomeFragment.newInstance() }
    private val somListFragment: Fragment? by lazy {
        sellerHomeRouter?.getSellerOrderManageFragment()
    }

    private val fragmentManger: FragmentManager by lazy {
        childFragmentManager
    }
    private var currentFragment: Fragment? = null

    private var hasAttachHomeFragment = false
    private var hasAttachSomFragment = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sah_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupDefaultPage()
    }

    private fun setupView() = view?.run {
        sahToolbar.setOnNotificationClickListener {
            println("setOnNotificationClickListener")
        }
        sahToolbar.showBadge()

        Handler().postDelayed({
            sahToolbar.hideBadge()
        }, 7000)
    }

    private fun setupDefaultPage() {
        val title = context?.getString(R.string.sah_home).orEmpty()
        currentFragment = homeFragment
        showFragment(FRAGMENT_HOME, title)
    }

    fun showFragment(fragmentType: String, title: String) {
        when (fragmentType) {
            FRAGMENT_HOME -> {
                if (!hasAttachHomeFragment) {
                    addFragment(homeFragment)
                    hasAttachHomeFragment = true
                }
                showFragment(homeFragment, title)
            }
            FRAGMENT_PRODUCT -> showFragment(homeFragment, title)
            FRAGMENT_ORDER -> somListFragment?.let {
                if (!hasAttachSomFragment) {
                    addFragment(it)
                    hasAttachSomFragment = true
                }
                showFragment(it, title)
            }
            FRAGMENT_OTHER -> showFragment(homeFragment, title)
        }
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
}