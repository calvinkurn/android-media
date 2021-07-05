package com.tokopedia.settingbank.view.viewModel

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        SettingBankViewModelTest::class,
        SelectBankViewModelTest::class,
        AddAccountViewModelTest::class,
        UploadDocumentViewModelTest::class)
class SettingBankTestSuit {
}