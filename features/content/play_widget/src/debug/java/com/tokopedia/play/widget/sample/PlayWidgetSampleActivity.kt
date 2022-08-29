package com.tokopedia.play.widget.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.play.widget.R
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleActivity : BaseSimpleActivity() {

    private val tabLayout by lazy { findViewById<TabsUnify>(R.id.play_tu_sample) }
    private val viewPager by lazy { findViewById<ViewPager2>(R.id.play_vp_sample) }

    private val pages = listOf<Pair<String, Fragment>>(
        Pair("Feed", PlayWidgetSampleFeedFragment()),
        Pair("Common",  PlayWidgetSampleCommonFragment())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_widget_sample)
        setupView()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun setupView() {
        viewPager.adapter = ViewPagerAdapter(this, pages)

        TabsUnifyMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomText(pages[position].first)
        }
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity,
                           private val pages: List<Pair<String, Fragment>>
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return pages[position].second
        }

        override fun getItemCount(): Int = pages.size
    }
}