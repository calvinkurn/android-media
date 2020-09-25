package com.tokopedia.abstraction.base.view.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.listener.FragmentLifecycleObserver

class CmViewPager : ViewPager {

    var pageChangeListener: ViewPager.SimpleOnPageChangeListener? = null
    var pageSwapped = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context) : super(context) {
        setup(null)
    }

    fun setup(attrs: AttributeSet?) {
        pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@CmViewPager.post {
                    pageSwapped = true
                    if (this@CmViewPager.adapter is CmPagerAdapter) {
                        val adapter = this@CmViewPager.adapter as CmPagerAdapter
                        val activeFragment = adapter.fragmentList[position]
                        FragmentLifecycleObserver.onFragmentSelected(activeFragment)
                    }
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        pageChangeListener?.let {
            addOnPageChangeListener(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pageChangeListener?.let {
            removeOnPageChangeListener(it)
        }
    }

    //Need to protect it from Firebase
    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        post {
            if (adapter is CmPagerAdapter && !pageSwapped) {
                if (currentItem == 0) {
                    val activeFragment = adapter.fragmentList[currentItem]
                    FragmentLifecycleObserver.onFragmentSelected(activeFragment)
                }
            }
        }
    }
}