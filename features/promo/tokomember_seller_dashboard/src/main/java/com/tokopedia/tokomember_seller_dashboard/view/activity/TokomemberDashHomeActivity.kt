package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.media.loader.clearImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashHomeViewpagerAdapter
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCouponFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramFragment
import com.tokopedia.unifycomponents.TabsUnify

class TokomemberDashHomeActivity : AppCompatActivity() {

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomember_dash_home)

        homeHeader = findViewById(R.id.home_header)
        homeTabs = findViewById(R.id.home_tabs)
        homeViewPager = findViewById(R.id.home_viewpager)

        homeHeader.apply {
            title = "TokoMember"
            isShowBackButton = true

            setNavigationOnClickListener {
                onBackPressed()
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
            feedbackIcon.setOnClickListener {
                 Toast.makeText(context, "Google form", Toast.LENGTH_SHORT).show()
            }

        }

        homeTabs.setupWithViewPager(homeViewPager)
        homeTabs.getUnifyTabLayout().setupWithViewPager(homeViewPager)

        var adapter = TokomemberDashHomeViewpagerAdapter(supportFragmentManager)
        adapter.addFragment(TokomemberDashHomeFragment.newInstance(), "Home")
        adapter.addFragment(TokomemberDashProgramFragment.newInstance(), "Program")
        adapter.addFragment(TokomemberDashCouponFragment.newInstance(), "Kupon Tokomember")

        homeViewPager.adapter = adapter

    }
}