package com.tokopedia.contactus.inboxticket2

import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterTest
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.CloseTicketByUserUseCaseTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCaseTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.InboxOptionUseCaseTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase2Test
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCaseTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.SubmitRatingUseCaseTest
import com.tokopedia.contactus.inboxticket2.domain.usecase.UploadImageUseCaseTest
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
