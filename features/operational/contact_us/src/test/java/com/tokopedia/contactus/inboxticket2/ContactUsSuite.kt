package com.tokopedia.contactus.inboxticket2

import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterTest
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterTest
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
        InboxDetailPresenterTest::class,
        InboxListPresenterTest::class
)
class ContactUsSuite