package com.tokopedia.profile.view.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.profile.R
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View {

    override lateinit var userSession: UserSession
    private var userId: String = "0"

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    companion object {
        fun createInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar()
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return recyclerView
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerProfileComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) {
        if (isLoadingInitialData) {
            presenter.getProfileFirstPage(userId)
        } else {
            presenter.getProfilePost(userId)
        }
    }

    override fun onSuccessGetProfileFirstPage(profileFirstPageViewModel: ProfileFirstPageViewModel,
                                              cursor: String) {
        val visitables: ArrayList<Visitable<*>> = ArrayList()
        visitables.add(profileFirstPageViewModel.profileHeaderViewModel)
        visitables.addAll(profileFirstPageViewModel.profilePostViewModel)
        renderList(visitables, !TextUtils.isEmpty(cursor))
    }

    override fun goToFollowing(userId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Going to following~", Toast.LENGTH_SHORT).show()
    }

    override fun followUnfollowUser(userId: Int, follow: Boolean) {
        //TODO milhamj
        Toast.makeText(context, "Follow? ".plus(follow).plus("~"), Toast.LENGTH_SHORT).show()
    }

    override fun goToProduct(productId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Going to product~", Toast.LENGTH_SHORT).show()
    }

    override fun addImages(productId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Adding more images", Toast.LENGTH_SHORT).show()
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    private fun initVar() {
        arguments?.let {
            userId = it.getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO)
        }
        userSession = UserSession(context)
    }

    private fun initView() {

    }
}