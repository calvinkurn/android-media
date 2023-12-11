package com.tokopedia.home_account.ui.accountsettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.constant.SettingConstant
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun AccountSettingScreen(
    state: AccountSettingUiModel?,
    modifier: Modifier = Modifier,
    additionalState: AdditionalState = AdditionalState(),
    onItemClicked: (Int) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (state) {
            AccountSettingUiModel.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NestLoader(
                        variant = NestLoaderType.Circular(NestLoaderSize.Small)
                    )
                }
            }

            is AccountSettingUiModel.Display -> {
                NestTypography(
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 8.dp),
                    text = stringResource(R.string.header_privacy_account_setting),
                    textStyle = NestTheme.typography.heading4.copy(fontWeight = FontWeight.Bold)
                )
                if (state.config.ubahKataSandi) {
                    ItemSetting(
                        stringResource(R.string.title_password_setting),
                        SettingConstant.SETTING_ACCOUNT_PASS_ID,
                        onClick = onItemClicked
                    )
                    ItemDivider()
                }
                ItemSetting(
                    stringResource(R.string.title_setting_pin),
                    SettingConstant.SETTING_PIN,
                    true,
                    onItemClicked
                )
                ItemDivider()
                if (additionalState.showBiometric) {
                    ItemSetting(
                        stringResource(R.string.title_fingerprint),
                        SettingConstant.SETTING_BIOMETRIC,
                        onClick = onItemClicked
                    )
                    ItemDivider()
                }
                if (state.config.dokumenDataDiri) {
                    ItemSetting(
                        stringResource(R.string.title_kyc_setting),
                        SettingConstant.SETTING_ACCOUNT_KYC_ID,
                        onClick = onItemClicked
                    )
                    ItemDivider()
                }
                if (additionalState.showSignInNotif) {
                    ItemSetting(
                        stringResource(R.string.title_signin_with_notification),
                        SettingConstant.SETTING_PUSH_NOTIF,
                        onClick = onItemClicked
                    )
                }
            }
        }
    }
}

data class AdditionalState(
    val showSignInNotif: Boolean = false,
    val showBiometric: Boolean = false
)

@Composable
private fun ItemSetting(
    text: String,
    settingId: Int,
    isNew: Boolean = false,
    onClick: (Int) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(settingId)
            }
            .padding(horizontal = 16.dp)

    ) {
        NestTypography(
            text,
            modifier = Modifier
                .defaultMinSize(minHeight = 64.dp)
                .padding(8.dp)
                .wrapContentHeight(),
            textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold)
        )
        if (isNew) {
            NestLabel(labelText = "NEW", labelType = NestLabelType.HIGHLIGHT_DARK_RED)
        }
    }
}

@Composable
private fun ItemDivider() {
    NestDivider(
        size = NestDividerSize.Small,
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
@Preview
fun AccountSettingPreview() {
    NestTheme {
        AccountSettingScreen(AccountSettingUiModel.Display())
    }
}
