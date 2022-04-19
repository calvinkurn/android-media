package com.tokopedia.profile.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.profile.R
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileEmptyTypeFactoryImpl
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * @author by milhamj on 22/10/18.
 */
class ProfileEmptyFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileEmptyContract.View {

    private var userId: Int = 0

    @Inject
    lateinit var presenter: ProfileEmptyContract.Presenter

    companion object {
        private const val SETTING_PROFILE_CODE = 83

        fun createInstance(bundle: Bundle): ProfileEmptyFragment {
            val fragment = ProfileEmptyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_empty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent

            DaggerProfileComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) = presenter.getProfileHeader(userId)

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileEmptyTypeFactoryImpl(this)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return view!!.findViewById(R.id.recyclerView)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view!!.findViewById(R.id.swipeToRefresh)
    }

    override fun getUserSession(): UserSession = UserSession(context)

    override fun onChangeAvatarClicked() {
        startActivityForResult(
                RouteManager.getIntent(context!!, ApplinkConst.SETTING_PROFILE),
                SETTING_PROFILE_CODE
        )
    }

    override fun goToFollowing() {
    }

    override fun goToFollower() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SETTING_PROFILE_CODE -> {
                onSwipeRefresh()
            }
        }
    }

    private fun initVar() {
        arguments?.let {
            try {
                userId = it
                        .getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO)
                        .toInt()
            } catch (e: java.lang.NumberFormatException) {
                activity?.finish()
            }
        }
    }
}