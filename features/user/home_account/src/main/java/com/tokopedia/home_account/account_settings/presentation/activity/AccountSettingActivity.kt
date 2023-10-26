package com.tokopedia.home_account.account_settings.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.home_account.R
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.nest.principles.ui.NestTheme
import javax.inject.Inject

class AccountSettingActivity : BaseSimpleActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: AccountSettingViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComponentFactory.instance
            .createHomeAccountComponent(this, application)
            .inject(this)
        toolbar.hide()
        setContent {
            val state by viewModel.state.observeAsState()

            NestTheme {
                Scaffold(topBar = {
                    NestHeader(
                        type = NestHeaderType.SingleLine(
                            title = stringResource(id = R.string.menu_account_title_security),
                            onBackClicked = { onBackClicked() }
                        )
                    )
                }, content = { padding ->
                        AccountSettingScreen(
                            state = state,
                            modifier = Modifier.padding(padding),
                            onItemClicked = ::onItemClicked
                        )
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0 -> {
                    NetworkErrorHelper.showSnackbar(
                        this,
                        getString(R.string.message_success_change_profile)
                    )
                    setResult(resultCode, Intent())
                }

                REQUEST_CHANGE_PASSWORD -> NetworkErrorHelper.showGreenCloseSnackbar(
                    this,
                    getString(
                        R.string.message_success_change_password
                    )
                )

                else -> {}
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onBackClicked() {
        finish()
    }

    private fun onItemClicked(id: Int) {
        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun getNewFragment(): Fragment? {
//        val bundle = Bundle()
//        if (intent.extras != null) {
//            bundle.putAll(intent.extras)
//        }
//        return AccountSettingFragment.createInstance(bundle)
        return null
    }

    companion object {
        private const val REQUEST_CHANGE_PASSWORD = 123
        fun createIntent(context: Context?): Intent {
            return Intent(context, AccountSettingActivity::class.java)
        }
    }
}
