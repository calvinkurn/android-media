package com.tokopedia.talk.inboxtalk.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import kotlinx.android.synthetic.main.fragment_inbox_talk.*

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = InboxTalkFragment()
    }

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_INBOX_TALK
    }

    override fun initInjector() {
        val inboxTalkComponent = DaggerInboxTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        inboxTalkComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        icon_filter.setButton1OnClickListener(showFilterDialog())
    }

    private fun showFilterDialog(): View.OnClickListener {
        return View.OnClickListener {
            context?.run {
                val menuItem = arrayOf(resources.getString(R.string.filter_all_talk),
                        resources.getString(R.string.filter_not_read))
                val filterMenu = Menus(this)
                filterMenu.setItemMenuList(menuItem)
                filterMenu.setActionText(getString(R.string.button_cancel))
                filterMenu.setOnActionClickListener { filterMenu.dismiss() }
                filterMenu.setOnItemMenuClickListener { itemMenus, pos -> onFilterClicked(pos) }
                filterMenu.show()
            }
        }
    }

    private fun onFilterClicked(pos: Int) {

    }
}