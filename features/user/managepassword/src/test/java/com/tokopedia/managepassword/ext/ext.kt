import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import io.mockk.MockKStubScope
import io.mockk.coEvery

inline fun MultiRequestGraphqlUseCase.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val it = this
    return coEvery { it.executeOnBackground() }
}