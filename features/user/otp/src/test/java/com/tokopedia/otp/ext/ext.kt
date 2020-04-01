import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.otp.validator.domain.usecase.ValidatorUseCase
import io.mockk.MockKStubScope
import io.mockk.coEvery

inline fun ValidatorUseCase<*>.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val it = this
    return coEvery { it.executeOnBackground() }
}

inline fun MultiRequestGraphqlUseCase.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val it = this
    return coEvery { it.executeOnBackground() }
}