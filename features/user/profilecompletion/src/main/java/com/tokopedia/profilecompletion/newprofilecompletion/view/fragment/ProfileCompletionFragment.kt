package com.tokopedia.profilecompletion.newprofilecompletion.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Pair
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.StatusPinData
import com.tokopedia.profilecompletion.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.newprofilecompletion.ProfileCompletionNewConstants
import com.tokopedia.profilecompletion.newprofilecompletion.view.activity.ProfileCompletionActivity
import com.tokopedia.profilecompletion.newprofilecompletion.view.listener.ProfileCompletionContract
import com.tokopedia.profilecompletion.newprofilecompletion.view.util.ProfileCompletionProgressBarAnimation
import com.tokopedia.profilecompletion.newprofilecompletion.data.ProfileCompletionDataView
import com.tokopedia.profilecompletion.newprofilecompletion.view.customview.ProfileCompletionTextDrawable
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileInfoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by stevenfredian on 6/22/17.
 */
class ProfileCompletionFragment : BaseDaggerFragment(), ProfileCompletionContract.View {

    private var progressBarTop: ProgressBar? = null
    private var viewPager: ViewPager? = null
    private var txtPercent: TextView? = null
    private var txtProceed: TextView? = null
    var progressBar: View? = null
    var content: View? = null
    var loading: View? = null
    var fragmentTransaction: FragmentTransaction? = null

    @Inject
    lateinit var currentUserSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val userInfoViewModel by lazy { viewModelProvider.get(ProfileInfoViewModel::class.java) }

    private var pinViewModel: AddChangePinViewModel? = null
    private var animationProfileCompletion: ProfileCompletionProgressBarAnimation? = null

    private var currentData: ProfileCompletionDataView? = null

    private var filled: String = ""
    private var skip: View? = null
    private var pair: Pair<Int, Int>? = null
    private var retryAction: NetworkErrorHelper.RetryClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState?.getParcelable<Parcelable?>(ARGS_DATA) != null) {
            currentData = savedInstanceState.getParcelable(ARGS_DATA)
        }
        pinViewModel = viewModelProvider.get(AddChangePinViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion, container, false)
        setHasOptionsMenu(true)
        initView(parentView)
        initialVar()
        initObserver()
        return parentView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, R.id.action_skip, 0, "")
        val menuItem = menu.findItem(R.id.action_skip) // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = draw
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initObserver() {
        pinViewModel?.getStatusPinResponse?.observe(viewLifecycleOwner, Observer { statusPinDataResult: Result<StatusPinData>? ->
            if (statusPinDataResult is Success<*>) {
                onSuccessGetStatusPin((statusPinDataResult as Success<StatusPinData>).data)
            } else if (statusPinDataResult is Fail) {
                onErrorGetStatusPin(statusPinDataResult.throwable)
            }
        })

        userInfoViewModel.userProfileInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    val displayModel = ProfileCompletionDataView()
                    displayModel.bday = it.data.birthDay
                    displayModel.completion = it.data.completionScore
                    displayModel.gender = it.data.gender
                    displayModel.phone = it.data.msisdn
                    displayModel.isPhoneVerified = it.data.isMsisdnVerified
                    onGetUserInfo(displayModel)
                }
                is Fail -> { onErrorGetUserInfo(ErrorHandler.getErrorMessage(context, it.throwable)) }
            }
        })
    }

    private fun onSuccessGetStatusPin(statusPinData: StatusPinData) {
        loading?.visibility = View.GONE
        if (!statusPinData.isRegistered &&
                currentUserSession.phoneNumber?.isNotEmpty() == true &&
                currentUserSession.isMsisdnVerified) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            startActivityForResult(intent, REQUEST_CODE_PIN)
        } else {
            activity?.finish()
        }
    }

    private fun onErrorGetStatusPin(throwable: Throwable) {
        loading?.visibility = View.GONE
        content?.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, view, throwable.message, retryAction)
    }

    private val draw: ProfileCompletionTextDrawable?
        get() {
            val drawable = context?.let { ProfileCompletionTextDrawable(it) }
            drawable?.text = resources.getString(R.string.skip_form)
            drawable?.setTextColor(com.tokopedia.unifyprinciples.R.color.Unify_N500)
            return drawable
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_skip) {
            skipView(findChildTag())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(view: View) {
        progressBar = view.findViewById(R.id.progress_bar)
        progressBarTop = view.findViewById(R.id.progressbar_top)
        viewPager = view.findViewById(R.id.viewpager)
        txtPercent = view.findViewById(R.id.txt_percent)
        viewPager = view.findViewById(R.id.viewpager)
        txtProceed = view.findViewById(R.id.txt_proceed)
        skip = view.findViewById(R.id.txt_skip)
        content = view.findViewById(R.id.content_new_profile_completion)
        loading = view.findViewById(R.id.loading_layout)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARGS_DATA, data)
    }

    private fun initialVar() {
        animationProfileCompletion = progressBarTop?.let { ProfileCompletionProgressBarAnimation(it) }
        filled = "filled"
        pair = Pair(R.anim.profilecompletion_slide_in_right, R.anim.profilecompletion_slide_out_left)
        retryAction = NetworkErrorHelper.RetryClickedListener {
            loading?.visibility = View.VISIBLE
            context?.let { userInfoViewModel.getUserProfileInfo(it) }
        }
        context?.let { userInfoViewModel.getUserProfileInfo(it) }
    }

    override fun onErrorGetUserInfo(string: String?) {
        loading?.visibility = View.GONE
        content?.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, view, string, retryAction)
    }

    private fun updateProgressBar(oldValue: Int, newValue: Int) {
        currentData?.completion = newValue
        animationProfileCompletion?.setValue(oldValue, newValue)
        animationProfileCompletion?.duration = 500
        progressBarTop?.startAnimation(animationProfileCompletion)
        progressBarTop?.progress = currentData?.completion?: 0
        txtPercent?.text = String.format("%s%%", progressBarTop?.progress.toString())

        val colors = resources.getIntArray(R.array.green_indicator)
        var indexColor = (newValue - 50) / 10
        if (indexColor < 0) {
            indexColor = 0
        }
        val shape = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.profilecompletion_horizontal_progressbar)
        } as LayerDrawable?

        val runningBar = (shape?.findDrawableByLayerId(R.id.progress_col) as ScaleDrawable).drawable as GradientDrawable

        runningBar.setColor(colors[indexColor])
        runningBar.mutate()
    }

    private fun loadFragment(profileCompletionDataView: ProfileCompletionDataView, pair: Pair<Int, Int>?) {
        KeyboardHandler.DropKeyboard(activity, view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fragmentTransaction = childFragmentManager.beginTransaction()
        }
        pair?.let { fragmentTransaction?.setCustomAnimations(pair.first, pair.second) }
        chooseFragment(profileCompletionDataView)
    }

    private fun chooseFragment(profileCompletionDataView: ProfileCompletionDataView) {
        if (checkingIsEmpty(profileCompletionDataView.gender.toString())) {
            val genderFragment = ProfileCompletionGenderFragment.createInstance(this)
            fragmentTransaction?.replace(R.id.fragment_container, genderFragment, ProfileCompletionGenderFragment.TAG)?.commit()

        } else if (checkingIsEmpty(profileCompletionDataView.bday)) {
            val dateFragment = ProfileCompletionDateFragment.createInstance(this)
            fragmentTransaction?.replace(R.id.fragment_container, dateFragment, ProfileCompletionDateFragment.TAG)?.commit()

        } else if (!profileCompletionDataView.isPhoneVerified) {
            val verifCompletionFragment = ProfileCompletionPhoneVerificationFragment.createInstance(this)
            fragmentTransaction?.replace(R.id.fragment_container, verifCompletionFragment, ProfileCompletionPhoneVerificationFragment.TAG)?.commit()

        } else if (profileCompletionDataView.completion == 100) {
            (activity as ProfileCompletionActivity?)?.onFinishedForm()

        } else {
            loading?.visibility = View.VISIBLE
            pinViewModel?.getStatusPin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PIN) {
            if (resultCode == Activity.RESULT_OK) {
                currentData?.completion?.let { updateProgressBar(it, it + 10) }
                setViewEnabled()
                currentData?.let { loadFragment(it, pair) }
            }
            activity?.finish()
        }
    }

    private fun checkingIsEmpty(item: String?): Boolean {
        return item == null || item.isEmpty() || item == "0" || item == DEFAULT_EMPTY_BDAY
    }

    override fun onSuccessEditProfile(edit: Int) {
        when (edit) {
            ProfileCompletionNewConstants.EDIT_DOB -> {
                currentData?.bday = filled
                currentData?.completion?.let {
                    updateProgressBar(it, it + 10)
                }
            }
            ProfileCompletionNewConstants.EDIT_GENDER -> {
                currentData?.gender = 3
                currentData?.completion?.let {
                    updateProgressBar(it, it + 10)
                }
            }
            ProfileCompletionNewConstants.EDIT_VERIF -> {
                currentData?.isPhoneVerified = true
                userSession?.setIsMSISDNVerified(true)
                currentData?.completion?.let {
                    updateProgressBar(it, it + 20) }
            }
        }
        setViewEnabled()
        currentData?.let { loadFragment(it, pair) }
    }

    private fun setViewEnabled() {
        progressBar?.visibility = View.GONE
        txtProceed?.visibility = View.VISIBLE
        canProceed(true)
        txtProceed?.text = getString(R.string.continue_form)
        skip?.isEnabled = true
    }

    override fun disableView() {
        progressBar?.visibility = View.VISIBLE
        txtProceed?.visibility = View.GONE
        skip?.isEnabled = false
    }

    override fun canProceed(answer: Boolean) {
        txtProceed?.isEnabled = answer
        if (answer) {
            txtProceed?.background?.setColorFilter(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G400), PorterDuff.Mode.SRC_IN)
            txtProceed?.setTextColor(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        } else {
            txtProceed?.background?.setColorFilter(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N100), PorterDuff.Mode.SRC_IN)
            txtProceed?.setTextColor(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N200))
        }
    }

    override fun onFailedEditProfile(errorMessage: String?) {
        setViewEnabled()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun findChildTag(): String? {
        val fragment = childFragmentManager.findFragmentById(R.id.fragment_container)
        return if (fragment != null) {
            fragment.tag
        } else ""
    }

    override val userSession: UserSessionInterface?
        get() = currentUserSession

    override fun skipView(tag: String?) {
        setViewEnabled()
        when (tag) {
            ProfileCompletionGenderFragment.TAG -> {
                currentData?.gender = 3
                currentData?.let { loadFragment(it, pair) }
            }
            ProfileCompletionDateFragment.TAG -> {
                currentData?.bday = filled
                currentData?.let { loadFragment(it, pair) }
            }
            ProfileCompletionPhoneVerificationFragment.TAG -> {
                currentData?.isPhoneVerified = true
                currentData?.let { loadFragment(it, pair) }
            }
            else -> {}
        }
    }

    override fun onGetUserInfo(profileCompletionDataView: ProfileCompletionDataView?) {
        content?.visibility = View.VISIBLE
        currentData = profileCompletionDataView
        currentData?.completion?.let { updateProgressBar(0, it) }
        loading?.visibility = View.GONE
        profileCompletionDataView?.let { loadFragment(it, Pair(0, 0)) }
    }

    override val data: ProfileCompletionDataView?
        get() = currentData

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    companion object {
        private const val DEFAULT_EMPTY_BDAY = "0001-01-01T00:00:00Z"
        private const val ARGS_DATA = "ARGS_DATA"
        private const val REQUEST_CODE_PIN = 200

        fun createInstance(): ProfileCompletionFragment {
            return ProfileCompletionFragment()
        }
    }
}