package com.tokopedia.profile.view.fragment

import android.content.Intent
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kol.feature.following_list.view.activity.KolFollowingListActivity
import com.tokopedia.profile.R
import com.tokopedia.profile.R.id.*
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject


/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View {

    override lateinit var userSession: UserSession
    private var userId: Int = 0

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    companion object {
        const val TEXT_PLAIN = "text/plain";

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
        if (!TextUtils.isEmpty(profileFirstPageViewModel.profileHeaderViewModel.affiliateName)) {
            addFooter(profileFirstPageViewModel.profileHeaderViewModel)
        }

        val visitables: ArrayList<Visitable<*>> = ArrayList()
        visitables.add(profileFirstPageViewModel.profileHeaderViewModel)
        visitables.addAll(profileFirstPageViewModel.profilePostViewModel)
        renderList(visitables, !TextUtils.isEmpty(cursor))
    }

    override fun goToFollowing() {
        startActivity(KolFollowingListActivity.getCallingIntent(context, userId))
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
            userId = it.getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO).toInt()
        }
        userSession = UserSession(context)
    }

    private fun initView() {

    }

    private fun addFooter(headerViewModel: ProfileHeaderViewModel) {
        footer.visibility = View.VISIBLE
        if (headerViewModel.isOwner) {
            footerOwn.visibility = View.VISIBLE
            footerOther.visibility = View.GONE

            if (!TextUtils.isEmpty(headerViewModel.recommendationQuota)) {
                recommendationQuota.visibility = View.VISIBLE
                recommendationQuota.text = headerViewModel.recommendationQuota
            } else {
                recommendationQuota.visibility = View.GONE
            }
            addRecommendation.setOnClickListener {
                RouteManager.route(context, ApplinkConst.AFFILIATE_EXPLORE)
            }
            shareOwn.setOnClickListener(shareLink(headerViewModel.link))
        } else {
            footerOwn.visibility = View.GONE
            footerOther.visibility = View.VISIBLE

            shareOther.setOnClickListener(shareLink(headerViewModel.link))
        }
    }

    private fun shareLink(link: String): View.OnClickListener {
        return View.OnClickListener {
            val shareBody = String.format(getString(R.string.profile_share_text), link)
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = TEXT_PLAIN
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(
                    Intent.createChooser(sharingIntent, getString(R.string.profile_share_title))
            )
        }
    }
}