package com.tokopedia.feedplus.view.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment
import com.tokopedia.videoTabComponent.view.VideoTabFragment

/**
 * @author by astidhiyaa on 30/08/22
 */
class FeedPlusTabAdapter(fm: FragmentManager, itemList: List<FeedTabs.FeedData>, bundle: Bundle?) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var itemList: List<FeedTabs.FeedData>
    private val bundle: Bundle?
    private val registeredFragment = SparseArrayCompat<Fragment>()

    init {
        this.itemList = itemList
        this.bundle = bundle
    }

    override fun getItem(position: Int): Fragment {
        val data = itemList[position]
        return if (data.type == FeedTabs.TYPE_FEEDS) {
            FeedPlusFragment.newInstance(bundle)
        } else if (data.type == FeedTabs.TYPE_EXPLORE) {
            ContentExploreFragment.newInstance(bundle)
        } else if (data.type == FeedTabs.TYPE_CUSTOM && data.key == FeedTabs.TYPE_VIDEO) {
            VideoTabFragment.newInstance(bundle)
        } else {
            /* Will be override for next to handle custom tab */
            Fragment()
        }
    }

    override fun getItemPosition(position: Any): Int {
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return itemList[position].title
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val f = super.instantiateItem(container, position) as Fragment
        registeredFragment.put(position, f)
        return f
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun setItemList(itemList: List<FeedTabs.FeedData>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun getRegisteredFragment(pos: Int): Fragment? {
        return registeredFragment[pos]
    }

    fun getContentExplore(): ContentExploreFragment? {
        val index = getContentExploreIndex()
        return if (getRegisteredFragment(index) is ContentExploreFragment) {
            getRegisteredFragment(index) as? ContentExploreFragment?
        } else {
            null
        }
    }

    private fun getContentVideo(): VideoTabFragment? {
        val index: Int = getVideoTabIndex()
        return if (getRegisteredFragment(index) is VideoTabFragment) {
            getRegisteredFragment(index) as? VideoTabFragment?
        } else {
            null
        }
    }

    fun getContentExploreIndex(): Int {
        for (i in 0 until count) {
            if (itemList[i].type == FeedTabs.TYPE_EXPLORE) {
                return i
            }
        }
        return 0
    }
    fun getVideoTabIndex(): Int {
        for (i in 0 until count) {
            if (itemList[i].type == FeedTabs.TYPE_CUSTOM && itemList[i].key == FeedTabs.TYPE_VIDEO) {
                return i
            }
        }
        return 0
    }

    fun isContextExploreExist(): Boolean {
        return getContentExplore() != null
    }
    fun isVideoTabExist(): Boolean {
        return getContentVideo() != null
    }
}
