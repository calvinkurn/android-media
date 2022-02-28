package com.tokopedia.createpost.uprofile.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.*
import com.tokopedia.createpost.uprofile.di.DaggerUserProfileComponent
import com.tokopedia.createpost.uprofile.di.UserProfileModule
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureFragmentAdapter
import com.tokopedia.seller_migration_common.presentation.fragment.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.up_layout_user_profile_header.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class FollowerListingFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var followersContainer: ViewFlipper? = null
    private var globalError: GlobalError? = null

    val userSessionInterface: UserSession by lazy {
        UserSession(context)
    }

    private val mPresenter: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }


    private val mAdapter: ProfileFollowersAdapter by lazy {
        ProfileFollowersAdapter(
            mPresenter,
            this,
            userSessionInterface,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        return inflater.inflate(R.layout.up_fragment_psger_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersContainer = view.findViewById(R.id.container)
        globalError = view?.findViewById(R.id.ge_followers)
        initObserver()
        initMainUi()
    }

    private fun initObserver() {
        addListObserver()
        addFollowersErrorObserver()
    }

    private fun initMainUi() {
        val rvFollowers = view?.findViewById<RecyclerView>(R.id.rv_followers)
        rvFollowers?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_NAME))
    }

    private fun refreshMainUi() {
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_NAME))
    }

    private fun addListObserver() =
        mPresenter.profileFollowersListLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {
                        mAdapter.resetAdapter()
                        mAdapter.notifyDataSetChanged()
                    }
                    is Success -> {
                        mAdapter.onSuccess(it.data)
                    }
                    is ErrorMessage -> {
                        mAdapter.onError()
                    }
                }
            }
        })

    private fun addFollowersErrorObserver() =
        mPresenter.followersErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        followersContainer?.displayedChild = 2
                        globalError?.setType(GlobalError.NO_CONNECTION)
                        globalError?.show()

                        globalError?.setActionClickListener {
                            followersContainer?.displayedChild = 1
                           refreshMainUi()
                        }
                    }
                    is IllegalStateException -> {
                        followersContainer?.displayedChild = 2
                        globalError?.setType(GlobalError.PAGE_FULL)
                        globalError?.show()

                        globalError?.setActionClickListener {
                            followersContainer?.displayedChild = 1
                            refreshMainUi()
                        }
                    }
                    is RuntimeException -> {
                        when (it.localizedMessage?.toIntOrNull()) {
                            ReponseStatus.NOT_FOUND -> {
                                followersContainer?.displayedChild = 2
                                globalError?.setType(GlobalError.PAGE_NOT_FOUND)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    followersContainer?.displayedChild = 1
                                    refreshMainUi()
                                }
                            }
                            ReponseStatus.INTERNAL_SERVER_ERROR -> {
                                followersContainer?.displayedChild = 2
                                globalError?.setType(GlobalError.SERVER_ERROR)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    followersContainer?.displayedChild = 1
                                    refreshMainUi()
                                }
                            }
                            else -> {
                                followersContainer?.displayedChild = 2
                                globalError?.setType(GlobalError.SERVER_ERROR)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    followersContainer?.displayedChild = 1
                                    refreshMainUi()
                                }
                            }
                        }
                    }
                }
            }
        })

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

    override fun onClick(source: View) {
        when (source.id) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserProfileFragment.REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            refreshMainUi()
        }
    }

    companion object {
        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowerListingFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        followersContainer?.displayedChild = 3 //emptypage

        followersContainer?.displayedChild = 3 //emptypage
        val textTitle = view?.findViewById<TextView>(R.id.text_error_empty_title)
        val textDescription = view?.findViewById<TextView>(R.id.text_error_empty_desc)

        textTitle?.text = "No follower"
        textDescription?.hide()
    }

    override fun onStartFirstPageLoad() {
        followersContainer?.displayedChild = 1
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        followersContainer?.displayedChild = 0

    }

    override fun onStartPageLoad(pageNumber: Int) {
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }
}

