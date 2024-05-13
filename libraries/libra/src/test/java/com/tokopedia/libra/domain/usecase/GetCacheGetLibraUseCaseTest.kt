package com.tokopedia.libra.domain.usecase

import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.model.ItemLibraUiModel
import com.tokopedia.libra.domain.model.LibraUiModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GetCacheGetLibraUseCaseTest {

    private val repository = mockk<CacheRepository>()

    @Test
    fun `should able to get a libra cache`() {
        // Given
        val owner = LibraOwner.Home
        val expectedValue = LibraUiModel(
            listOf(
                ItemLibraUiModel("foo", "bar")
            )
        )

        every { repository.get(owner) } returns expectedValue

        // When
        val useCase = GetLibraCacheUseCase(repository)
        val result = useCase(owner)

        // Then
        assert(result == expectedValue)
    }

    @Test
    fun `should able to clear partial cache based on owner`() {
        // Given
        val owner = LibraOwner.Home
        every { repository.clear(owner) } just Runs

        // When
        val useCase = GetLibraCacheUseCase(repository)
        useCase.clear(owner)

        // Then
        verify(atLeast = 1) { repository.clear(any()) }
    }
}
