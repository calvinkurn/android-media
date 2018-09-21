package com.tokopedia.profile.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.user.session.UserSession

/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View {

    override lateinit var userSession: UserSession

    companion object {
        fun createInstance() = ProfileFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) {
    }

    override fun goToFollowing(userId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Going to following~", Toast.LENGTH_LONG).show()
    }

    override fun followUnfollowUser(userId: Int, follow: Boolean) {
        //TODO milhamj
    }

    private fun initVar() {
        userSession = UserSession(context)
    }
}