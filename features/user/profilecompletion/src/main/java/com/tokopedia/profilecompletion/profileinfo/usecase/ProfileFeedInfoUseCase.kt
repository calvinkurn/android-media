package com.tokopedia.profilecompletion.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedResponse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileFeedInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : CoroutineUseCase<Unit, ProfileFeedResponse>(Dispatchers.IO) {

    /* can use both username/userId as param */
    private val usernameParam = "username"

    override fun graphqlQuery(): String {
        return """query feedXProfileForm(${'$'}username: String!) {
                    feedXProfileForm(username: ${'$'}username) {
                        profile {
                            username
                            biography
                            sharelink {
                                weblink
                                applink
                            }
                            canChangeUsername
                            profilePreviewImageURL
                        }
                        userProfileConfiguration {
                            usernameConfiguration {
                               maximumChar
                               minimumChar
                            }
                        biographyConfiguration {
                            maximumChar
                        }
                    }
                }
            }""".trimIndent()
    }

    override suspend fun execute(params: Unit): ProfileFeedResponse {
        return repository.request(graphqlQuery(), mapOf(usernameParam to userSession.userId))
    }
}
