package com.tokopedia.home_account.stub.di

import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.di.HomeAccountUserScope
import com.tokopedia.home_account.test.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HomeAccountResponseModule {

    @HomeAccountUserScope
    @Named("CentralizedUserAssetDataModelSuccess")
    @Provides
    fun provideCentralizedUserAssetDataModelSuccess(): CentralizedUserAssetDataModel = AndroidFileUtil.parse(
        R.raw.success_get_centralized_user_asset_config,
        CentralizedUserAssetDataModel::class.java
    )

//    @HomeAccountUserScope
//    @Named("CentralizedUserAssetDataModelFailed")
//    @Provides
//    fun provideCentralizedUserAssetDataModelFailed(): CentralizedUserAssetDataModel = AndroidFileUtil.parse(
//        "centralized_user_asset_data_model_failed.json",
//        CentralizedUserAssetDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("GopayBalanceAndPointDataModelSuccess")
//    @Provides
//    fun provideGopayBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "gopay_balance_and_point_data_model_success.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("GopayBalanceAndPointDataModelFailed")
//    @Provides
//    fun provideGopayBalanceAndPointDataModelFailed(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "gopay_balance_and_point_data_model_failed.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("TokopointBalanceAndPointDataModelSuccess")
//    @Provides
//    fun provideTokopointBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "tokopoint_balance_and_point_data_model_success.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("TokopointBalanceAndPointDataModelFailed")
//    @Provides
//    fun provideTokopointBalanceAndPointDataModelFailed(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "tokopoint_balance_and_point_data_model_failed.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("SaldoBalanceAndPointDataModelSuccess")
//    @Provides
//    fun provideSaldoBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "saldo_balance_and_point_data_model_success.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("SaldoBalanceAndPointDataModelFailed")
//    @Provides
//    fun provideSaldoBalanceAndPointDataModelFailed(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "saldo_balance_and_point_data_model_failed.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("CobrandCCBalanceAndPointDataModelSuccess")
//    @Provides
//    fun provideCobrandCCBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "cobrand_cc_balance_and_point_data_model_success.json",
//        BalanceAndPointDataModel::class.java
//    )
//
//    @HomeAccountUserScope
//    @Named("CobrandCCBalanceAndPointDataModelFailed")
//    @Provides
//    fun provideCobrandCCBalanceAndPointDataModelFailed(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "cobrand_cc_balance_and_point_data_model_failed.json",
//        BalanceAndPointDataModel::class.java
//    )
}