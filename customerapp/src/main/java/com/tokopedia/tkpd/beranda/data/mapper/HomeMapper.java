package com.tokopedia.tkpd.beranda.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.beranda.data.source.pojo.HomeData;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<Response<GraphqlResponse<HomeData>>, List<Visitable>> {
    @Override
    public List<Visitable> call(Response<GraphqlResponse<HomeData>> response) {
        if (response.isSuccessful()) {
            List<Visitable> list = new ArrayList<>();

            HomeData homeData = response.body().getData();

            if (homeData.getTicker() != null && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            /*if (homeData.getSlides() != null && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides()));
            }*/

            return list;
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private Visitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private Visitable mappingBanner(List<BannerSlidesModel> slides) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(slides);
        return viewModel;
    }
}
