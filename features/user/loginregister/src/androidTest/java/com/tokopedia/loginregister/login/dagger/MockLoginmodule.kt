package com.tokopedia.loginregister.login.dagger

import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginregister.login.di.LoginModule
import com.tokopedia.loginregister.login.stub.MockCryptography
import dagger.Module
import dagger.Provides

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
class MockLoginmodule: LoginModule() {

    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    override fun provideCryptographyUtils(): Cryptography? = MockCryptography()
}