package com.tokopedia.contactus.inboxticket2

import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImplTest
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterImplTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        CloseTicketByUserUseCaseTest::class,
        GetTicketListUseCaseTest::class,
        InboxOptionUseCaseTest::class,
        PostMessageUseCase2Test::class,
        PostMessageUseCaseTest::class,
        SubmitRatingUseCaseTest::class,
        UploadImageUseCaseTest::class,
        InboxDetailPresenterImplTest::class,
        InboxListPresenterImplTest::class
)
class ContactUsSuite