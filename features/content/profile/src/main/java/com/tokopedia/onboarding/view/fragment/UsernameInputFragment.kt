package com.tokopedia.onboarding.view.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.onboarding.TERMS_AND_CONDITIONS_URL
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.view.adapter.SuggestionAdapter
import com.tokopedia.onboarding.view.listener.UsernameInputContract
import com.tokopedia.profile.R
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_af_username_input.*
import javax.inject.Inject

/**
 * @author by milhamj on 9/24/18.
 */
class UsernameInputFragment : BottomSheetDialogFragment(), UsernameInputContract.View {

    private val adapter: SuggestionAdapter by lazy {
        SuggestionAdapter(this)
    }
    private val textWatcher: AfterTextWatcher by lazy {
        object : AfterTextWatcher() {
            override fun afterTextChanged(editable: Editable) {
                val byMeUsername = usernameInput.textWithoutPrefix

                if (byMeUsername.length > USERNAME_MAX_LENGTH) {
                    usernameInput.removeTextChangedListener(this)
                    usernameInput.setText(byMeUsername.substring(0, USERNAME_MAX_LENGTH))
                    usernameInput.addTextChangedListener(this)
                }

                if (byMeUsername.length < USERNAME_MIN_LENGTH) {
                    usernameWrapper.error = String.format(
                            getString(R.string.af_minimal_character),
                            USERNAME_MIN_LENGTH)
                    disableSaveBtn()
                } else {
                    usernameWrapper.error = null
                    enableSaveBtn()
                }

                if (byMeUsername.length < SHOW_SUGGESTION_LENGTH && adapter.itemCount > 0) {
                    suggestionCard.visibility = View.VISIBLE
                } else {
                    suggestionCard.visibility = View.GONE
                }
            }
        }
    }

    @Inject
    lateinit var presenter: UsernameInputContract.Presenter

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_username_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        initVar()
        initView()
        presenter.getUsernameSuggestion()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    override fun onSuccessGetUsernameSuggestion(suggestions: List<String>) {
        adapter.setList(suggestions)
        suggestionCard.visibility = View.VISIBLE

        setFirstSuggestion(suggestions)
    }

    override fun onSuggestionClicked(username: String) {
        usernameInput.setText(username.toLowerCase())
    }

    override fun onSuccessRegisterUsername() {
        activity?.finish()
    }

    override fun onErrorRegisterUsername(message: String) {
        NetworkErrorHelper.showRedSnackbar(mainView, message)
    }

    override fun onErrorInputRegisterUsername(message: String) {
        usernameWrapper.error = message
    }

    private fun initVar() {

    }

    private fun initInjector() {
        val baseAppComponent = (activity!!.application as BaseMainApplication)
                .baseAppComponent
        DaggerOnboardingComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initView() {
        avatar?.loadImageCircle(userSession.profilePicture)
        usernameInput?.addTextChangedListener(textWatcher)
        suggestionRv?.adapter = adapter
        saveBtn?.setOnClickListener {
            registerUsername()
        }
        disableSaveBtn()
        termsAndCondition?.setOnClickListener {
            RouteManager.route(
                    context,
                    String.format(
                            "%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            TERMS_AND_CONDITIONS_URL
                    )
            )
        }
    }

    private fun enableSaveBtn() {
        saveBtn.isEnabled = true
    }

    private fun disableSaveBtn() {
        saveBtn.isEnabled = false
    }

    private fun registerUsername() {
        presenter.registerUsername(usernameInput.textWithoutPrefix)
    }

    private fun setFirstSuggestion(suggestions: List<String>?) {
        if (suggestions == null || suggestions.isEmpty()) {
            return
        }
        usernameInput.setText(suggestions[0])
    }

    companion object {

        private const val USERNAME_MAX_LENGTH = 15
        private const val USERNAME_MIN_LENGTH = 3
        private const val SHOW_SUGGESTION_LENGTH = 1
        private const val PARAM_USER_ID = "{user_id}"

        fun newInstance(bundle: Bundle): UsernameInputFragment {
            val fragment = UsernameInputFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
