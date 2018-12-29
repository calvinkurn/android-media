package com.tokopedia.loginregister.discover.mapper;

import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;
import com.tokopedia.loginregister.discover.pojo.DiscoverItemPojo;
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverMapper implements Func1<Response<DataResponse<DiscoverPojo>>,
        DiscoverViewModel> {

    @Inject
    public DiscoverMapper() {
    }

    @Override
    public DiscoverViewModel call(Response<DataResponse<DiscoverPojo>> response) {

        if (response.isSuccessful()) {
            return mappingToViewModel(response.body().getData());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private DiscoverViewModel mappingToViewModel(DiscoverPojo pojo) {
        return new DiscoverViewModel(
                convertToDiscoverItem(pojo.getProviders()),
                pojo.getUrlBackground()
        );
    }

    private ArrayList<DiscoverItemViewModel> convertToDiscoverItem(List<DiscoverItemPojo> providers) {
        ArrayList<DiscoverItemViewModel> list = new ArrayList<>();
        for (DiscoverItemPojo pojo : providers) {
            list.add(new DiscoverItemViewModel(pojo.getId(),
                    pojo.getName(),
                    pojo.getUrl(),
                    pojo.getImage(),
                    pojo.getColor()));
        }
        return list;
    }
}
