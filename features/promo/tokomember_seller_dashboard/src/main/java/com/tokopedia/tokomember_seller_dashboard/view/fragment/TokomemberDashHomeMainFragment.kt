package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.clearImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ACTION
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashHomeViewpagerAdapter
import com.tokopedia.unifycomponents.TabsUnify
import javax.inject.Inject

class TokomemberDashHomeMainFragment : BaseDaggerFragment() {

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager
    private lateinit var homeFragmentCallback: TmProgramDetailCallback
    private var programActionType = -1

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TmProgramDetailCallback) {
            homeFragmentCallback =  context as TmProgramDetailCallback
        } else {
            throw RuntimeException(requireContext().toString() )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_home_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeHeader = view.findViewById(R.id.home_header)
        homeTabs = view.findViewById(R.id.home_tabs)
        homeViewPager = view.findViewById(R.id.home_viewpager)
        programActionType = arguments?.getInt(BUNDLE_PROGRAM_ACTION)?:0

        homeHeader.apply {
            title = "TokoMember"
            isShowBackButton = true
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.finish()
            }

            val feedbackIcon = addRightIcon(0)
            feedbackIcon.clearImage()
            feedbackIcon.setImageDrawable(getIconUnifyDrawable(context, IconUnify.CHAT_REPORT))
            feedbackIcon.setColorFilter(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            feedbackIcon.hide()
            feedbackIcon.setOnClickListener {
                Toast.makeText(context, "Google form", Toast.LENGTH_SHORT).show()
            }

        }

        homeTabs.setupWithViewPager(homeViewPager)
        homeTabs.getUnifyTabLayout().setupWithViewPager(homeViewPager)

        val adapter = TokomemberDashHomeViewpagerAdapter(childFragmentManager)
        adapter.addFragment(TokomemberDashHomeFragment.newInstance(arguments), "Home")
        adapter.addFragment(TokomemberDashProgramListFragment.newInstance(arguments), "Program")
        adapter.addFragment(TokomemberDashCouponFragment.newInstance(arguments), "Kupon Tokomember")

        homeViewPager.adapter = adapter
        homeViewPager.offscreenPageLimit = 3

        homeTabs.getUnifyTabLayout().removeAllTabs()
        homeTabs.customTabMode = TabLayout.MODE_SCROLLABLE
        homeTabs.addNewTab("Home")
        homeTabs.addNewTab("Program")
        homeTabs.addNewTab("Kupon Tokomember")

        if (programActionType!=-1) {
            setTabsProgramList()
        }
    }

    private fun setTabsProgramList(){
        homeTabs.getUnifyTabLayout().getTabAt(1)?.select()
        homeViewPager.currentItem = 1
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        const val TAG_HOME = "tag_home"
        fun newInstance(extras: Bundle?) = TokomemberDashHomeMainFragment().apply {
            arguments = extras
        }
    }
}