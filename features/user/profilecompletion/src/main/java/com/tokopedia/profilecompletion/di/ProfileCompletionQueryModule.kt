package com.tokopedia.profilecompletion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addemail.data.CheckEmailPojo
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.CheckPhonePojo
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changename.data.ChangeNamePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.settingprofile.data.SubmitProfilePictureData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileInfoData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileRoleData
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey


@ProfileCompletionSettingScope
@Module
class ProfileCompletionQueryModule {

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CHANGE_NAME)
    fun provideRawMutationChangeName(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_name)

    @Provides
    fun provideChangeNameGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChangeNamePojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CHANGE_GENDER)
    fun provideRawMutationChangeGender(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_gender)

    @Provides
    fun provideChangeGenderGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChangeGenderPojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CHECK_EMAIL)
    fun provideRawMutationCheckEmail(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_check_email)


    @Provides
    fun provideCheckEmailGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<CheckEmailPojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_ADD_EMAIL)
    fun provideRawMutationAddEmail(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_email)


    @Provides
    fun provideAddEmailGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<AddEmailPojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_ADD_PHONE)
    fun provideRawMutationAddPhone(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_phone)

    @Provides
    fun provideAddPhoneGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<AddPhonePojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CHECK_PHONE)
    fun provideRawMutationCheckPhone(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_check_phone)

    @Provides
    fun provideCheckPhoneGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<CheckPhonePojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CHANGE_PICTURE)
    fun provideRawMutationChangePicture(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_picture)

    @Provides
    fun provideChangePictureUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<SubmitProfilePictureData> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_ADD_BOD)
    fun provideRawMutationAddBod(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_bod)

    @Provides
    fun provideAddBodUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<UserProfileCompletionUpdateBodData> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionSettingScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_PROFILE_COMPLETION)
    fun provideRawQueryProfileCompletion(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_user_profile_completion)


    @Provides
    fun provideUserProfileInfoUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<UserProfileInfoData> = GraphqlUseCase<UserProfileInfoData>(graphqlRepository).apply {
        setTypeClass(UserProfileInfoData::class.java)
    }

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE)
    fun provideRawQueryProfileRole(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_user_profile_role)


    @Provides
    fun provideUserProfileRoleUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<UserProfileRoleData> = GraphqlUseCase<UserProfileRoleData>(graphqlRepository).apply {
        setTypeClass(UserProfileRoleData::class.java)
    }

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_USER_VALIDATE)
    fun provideRawQueryUserValidate(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_user_profile_completion_validate)


    @Provides
    fun provideUserValidateGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<UserValidatePojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_CREATE_PIN)
    fun provideRawMutationCreatePin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_create_pin)

    @Provides
    fun provideAddPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<AddPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.MUTATION_UPDATE_PIN)
    fun provideRawMutationUpdatePin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_update_pin)

    @Provides
    fun provideUpdatePinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChangePinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_CHECK_PIN)
    fun provideRawQueryCheckPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_check_pin)

    @Provides
    fun provideCheckPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<CheckPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_GET_STATUS_PIN)
    fun provideRawQueryGetStatusPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_status_pin)


    @Provides
    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN)
    fun provideRawQueryValidatePin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_validate_pin)


    @Provides
    fun provideValidatePinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ValidatePinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueryConstant.QUERY_SKIP_OTP_PIN)
    fun provideRawQuerySkipOtpPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_skip_otp_pin)


    @Provides
    fun provideSkipOtpPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<SkipOtpPinPojo> = GraphqlUseCase(graphqlRepository)
}