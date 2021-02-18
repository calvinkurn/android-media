package com.tokopedia.loginregister.login.dagger

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

//@Module
//open class MockLoginmodule: LoginModule() {
//
//    override fun provideLocalCacheHandler(context: Context): LocalCacheHandler {
//        return LocalCacheHandler(context, "LOGIN_CACHE")
//    }
//
//    override fun provideMainDispatcher(): CoroutineDispatcher {
//        return Main
//    }
//
//    override fun provideCryptographyUtils(): Cryptography? = MockCryptography()
//
//    override fun provideFingerprintSetting(context: Context): FingerprintSetting {
//        return FingerprintPreferenceHelper(context)
//    }
//
//    override fun provideSocmedBottomSheet(context: Context): SocmedBottomSheet {
//        return SocmedBottomSheet(context)
//    }
//
//    override fun provideDispatcherProvider(): DispatcherProvider {
//        return object : DispatcherProvider {
//            override fun io(): CoroutineDispatcher {
//                return IO
//            }
//
//            override fun ui(): CoroutineDispatcher {
//                return Main
//            }
//        }
//    }
//}