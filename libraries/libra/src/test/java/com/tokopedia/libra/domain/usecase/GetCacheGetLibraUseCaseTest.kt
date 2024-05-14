package com.tokopedia.libra.domain.usecase

import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.robot.createSetLibraUseCaseRobot
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
        val robot = createSetLibraUseCaseRobot(cacheRepository = repository)
        every { repository.get(robot.owner) } returns robot.libraUiModel

        // When
        val useCase = GetLibraCacheUseCase(repository)
        val result = useCase(robot.owner)

        // Then
        assert(result == robot.libraUiModel)
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
