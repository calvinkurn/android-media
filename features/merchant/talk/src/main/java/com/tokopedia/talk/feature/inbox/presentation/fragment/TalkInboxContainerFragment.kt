package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxContainerAdapter
import com.tokopedia.talk_old.R
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_talk_inbox_container.*
import javax.inject.Inject

class TalkInboxContainerFragment : BaseDaggerFragment(), HasComponent<TalkInboxContainerComponent> {

    companion object {
        fun createNewInstance(): TalkInboxContainerFragment {
            return TalkInboxContainerFragment()
        }
    }
    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_talk_inbox_container, container, false)
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): TalkInboxContainerComponent? {
        return activity?.run {
            DaggerTalkInboxContainerComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupAdapter()
    }

    private fun setupViewPager() {
        val tabTitles = getTabTitles()
        tabTitles.forEach {
            talkInboxTabs.addNewTab(it)
        }
        talkInboxViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                talkInboxTabs.getUnifyTabLayout().getTabAt(position)
            }
        })
    }

    private fun setupAdapter() {
        talkInboxViewPager.adapter = TalkInboxContainerAdapter(getFragmentList(), this)
    }

    private fun getFragmentList(): List<Fragment> {
        return listOf(
                TalkInboxFragment.createNewInstance(TalkInboxTab.TalkBuyerInboxTab()),
                TalkInboxFragment.createNewInstance(TalkInboxTab.TalkShopInboxTab())
        )
    }

    private fun getTabTitles(): List<String> {
        return with(userSession) {
            listOf(shopName, name)
        }
    }


}