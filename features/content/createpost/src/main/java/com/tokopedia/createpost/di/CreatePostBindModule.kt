package com.tokopedia.createpost.di

import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.common.domain.entity.FeedDetail
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.domain.usecase.GetFeedForEditUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetProfileHeaderUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
@Module(includes = [CreatePostCommonModule::class])
abstract class CreatePostBindModule {

//    @Binds
//    @CreatePostScope
//    abstract fun bindGetContentFormUseCase(usecase: GetContentFormUseCase): UseCase<GetContentFormDomain>
//
//    @Binds
//    @CreatePostScope
//    abstract fun bindGetFeedUseCase(usecase: GetFeedForEditUseCase): UseCase<FeedDetail?>
//
//    @Binds
//    @CreatePostScope
//    abstract fun bindGetProfileHeaderUserCase(usecase: GetProfileHeaderUseCase): GraphqlUseCase

    /**
     * TODO: will uncomment this later when these 2 dependencies can be be mocked
     * */
//    @Binds
//    @CreatePostScope
//    abstract fun bindGetProductSuggestionUseCase(usecase: GetProductSuggestionUseCase): UseCase<List<ProductSuggestionItem>>
//
//    @Binds
//    @CreatePostScope
//    abstract fun bindGetShopFavoriteStatusUseCase(usecase: GQLGetShopFavoriteStatusUseCase): UseCase<ShopInfo>
}