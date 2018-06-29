package com.tokopedia.contactus.inboxticket2.domain.usecase;


import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;

import java.util.List;

/**
 * Created by pranaymohapatra on 28/06/18.
 */

public class GetTicketDetailUseCase extends RestRequestUseCase {
    private int ticketId;

    @Override
    protected List<RestRequest> buildRequest() {
        return null;
    }
}
