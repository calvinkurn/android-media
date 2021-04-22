package com.tokopedia.digital_deals.view.presenter;

import android.text.TextUtils;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetDealLikesUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetEventContentUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase;
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.digital_deals.domain.postusecase.PostNsqTravelDataUseCase;
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqEntertainmentModel;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqMessage;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqRecentDataModel;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqRecentSearchModel;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqServiceModel;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqTravelRecentSearchModel;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.response.EventContentData;
import com.tokopedia.digital_deals.view.model.response.GetLikesResponse;
import com.tokopedia.digital_deals.view.model.response.SearchResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.usecase.RequestParams;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class DealDetailsPresenter extends BaseDaggerPresenter<DealDetailsContract.View>
        implements DealDetailsContract.Presenter {

    private int currentPage, totalPages;
    private GetDealDetailsUseCase getDealDetailsUseCase;
    private GetSearchNextUseCase getSearchNextUseCase;
    private GetDealLikesUseCase getDealLikesUseCase;
    private PostNsqEventUseCase postNsqEventUseCase;
    private PostNsqTravelDataUseCase postNsqTravelDataUseCase;
    private DealsDetailsResponse dealsDetailsResponse;
    private GetEventContentUseCase getEventContentUseCase;
    public static final String HOME_DATA = "home_data";
    public final static String TAG = "url";
    public final static boolean DEFAULT_PARAM_ENABLE = true;
    public final static String PARAM_DEAL_PASSDATA = "PARAM_DEAL_PASSDATA";
    private TouchViewPager mTouchViewPager;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams = RequestParams.create();

    private static final int SALAM_INDICATOR = 0;
    private static final int SALAM_VALUE = 131072;
    private static final String typeId = "4";

    @Inject
    public DealDetailsPresenter(GetDealDetailsUseCase getDealDetailsUseCase,
                                GetSearchNextUseCase getSearchNextUseCase,
                                GetDealLikesUseCase getDealLikesUseCase,
                                PostNsqEventUseCase postNsqEventUseCase,
                                PostNsqTravelDataUseCase postNsqTravelDataUseCase,
                                GetEventContentUseCase getEventContentUseCase) {
        this.getDealDetailsUseCase = getDealDetailsUseCase;
        this.getSearchNextUseCase = getSearchNextUseCase;
        this.getDealLikesUseCase = getDealLikesUseCase;
        this.postNsqEventUseCase = postNsqEventUseCase;
        this.postNsqTravelDataUseCase = postNsqTravelDataUseCase;
        this.getEventContentUseCase = getEventContentUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getDealDetailsUseCase.unsubscribe();
        getSearchNextUseCase.unsubscribe();
        postNsqEventUseCase.unsubscribe();
        postNsqTravelDataUseCase.unsubscribe();
    }

    public void getDealDetails() {

        getView().showProgressBar();
        getView().hideCollapsingHeader();
        getDealDetailsUseCase.setRequestParams(getView().getParams());
        getDealDetailsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                Timber.d("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                getView().hideCollapsingHeader();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getDealDetails();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                dealsDetailsResponse = (DealsDetailsResponse) data.getData();
                if (dealsDetailsResponse != null )
                    Utils.getSingletonInstance().sortOutletsWithLocation(dealsDetailsResponse.getOutlets(), Utils.getSingletonInstance().getLocation(getView().getActivity()));
                getView().hideProgressBar();
                getView().showShareButton();
                getView().showCollapsingHeader();
                getView().renderDealDetails(dealsDetailsResponse);
                searchNextParams.putString(Utils.NEXT_URL, dealsDetailsResponse.getRecommendationUrl());
                if (getView().isRecommendationEnableFromArguments()){
                    getRecommendedDeals();
                }else {
                    getView().hideRecomendationDealsView();
                }
                Timber.d("enter onNext");

                if (!getView().isEnableBuyFromArguments()){
                    getView().hideCheckoutView();
                }

                if (!getView().isEnableLikeFromArguments()){
                    getView().hideLikeButtonView();
                } else {
                    getLikes();
                }
            }
        });
    }

    private void getLikes() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("deal_id", String.valueOf(dealsDetailsResponse.getId()));
        getDealLikesUseCase.setRequestParams(requestParams);
        getDealLikesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<ArrayList<GetLikesResponse>>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();

                ArrayList<GetLikesResponse> getLikesResponse = (ArrayList<GetLikesResponse>) dataResponse.getData();
                if (getLikesResponse != null && getLikesResponse.size() > 0) {
                    getView().setLikes(getLikesResponse.get(0).getTotalLikes(), getLikesResponse.get(0).isLiked());
                }

            }
        });
    }

    private void getRecommendedDeals() {

        isLoading = true;
        getSearchNextUseCase.setRequestParams(searchNextParams);
        getSearchNextUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if ((dealsDetailsResponse.customText1 & SALAM_VALUE) <= SALAM_INDICATOR){
                    Type token = new TypeToken<DataResponse<SearchResponse>>() {
                    }.getType();
                    RestResponse restResponse = typeRestResponseMap.get(token);
                    DataResponse data = restResponse.getData();
                    SearchResponse searchResponse = (SearchResponse) data.getData();

                    isLoading = false;
                    getView().removeFooter();

                    getView().addDealsToCards((ArrayList<ProductItem>) processSearchResponse(searchResponse));

                    checkIfToLoad(getView().getLayoutManager());
                }
            }
        });
    }

    private List<ProductItem> processSearchResponse(SearchResponse searchResponse) {

        String nexturl = searchResponse.getPage().getUriNext();
        if (!TextUtils.isEmpty(nexturl)) {
            searchNextParams.putString(Utils.NEXT_URL, nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
        return searchResponse.getDeals();
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == com.tokopedia.digital_deals.R.id.action_menu_share) {
            Utils.getSingletonInstance().shareDeal(dealsDetailsResponse.getSeoUrl(),
                    getView().getActivity(), dealsDetailsResponse.getDisplayName(),
                    dealsDetailsResponse.getImageWeb(), dealsDetailsResponse.getWebUrl());
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onBannerSlide(int page) {
        currentPage = page;
    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                    }
                });
    }

    @Override
    public List<Outlet> getAllOutlets() {
        if (dealsDetailsResponse != null)
            return dealsDetailsResponse.getOutlets();
        else
            return null;
    }

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                getRecommendedDeals();
            } else {
                getView().addFooter();
            }
        }
    }

    public void sendNsqEvent(String userId, DealsDetailsResponse data) {
        NsqServiceModel nsqServiceModel = new NsqServiceModel();
        nsqServiceModel.setService(Utils.NSQ_SERVICE);
        NsqMessage nsqMessage = new NsqMessage();
        nsqMessage.setUserId(Integer.parseInt(userId));
        nsqMessage.setProductId(data.getId());
        nsqMessage.setUseCase(Utils.NSQ_USE_CASE);
        nsqMessage.setAction("product-detail");
        nsqServiceModel.setMessage(nsqMessage);
        postNsqEventUseCase.setRequestModel(nsqServiceModel);
        postNsqEventUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Timber.d("enter error");
                throwable.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), () -> sendNsqEvent(userId, data));
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Log.d("Naveen", " Deal Detail NSQ Event 1");
            }
        });

    }

    public void sendNsqTravelEvent(String userId, DealsDetailsResponse data) {
        NsqTravelRecentSearchModel nsqTravelRecentSearchModel = new NsqTravelRecentSearchModel();
        NsqServiceModel nsqServiceModel1 = new NsqServiceModel();
        nsqTravelRecentSearchModel.setService("travel_recent_search");
        NsqMessage nsqMessage1 = new NsqMessage();
        nsqMessage1.setUserId(Integer.parseInt(userId));
        nsqTravelRecentSearchModel.setNsqMessage(nsqMessage1);
        NsqRecentSearchModel nsqRecentSearchModel = new NsqRecentSearchModel();
        nsqRecentSearchModel.setDataType("deal");
        NsqRecentDataModel nsqRecentDataModel = new NsqRecentDataModel();
        NsqEntertainmentModel nsqEntertainmentModel = new NsqEntertainmentModel();
        nsqEntertainmentModel.setValue(data.getDisplayName());
        nsqEntertainmentModel.setPrice(Utils.getSingletonInstance().convertToCurrencyString(data.getSalesPrice()));
        nsqEntertainmentModel.setImageUrl(data.getMediaUrl().get(0).getUrl());
        nsqEntertainmentModel.setId(data.getBrand().getTitle());
        nsqEntertainmentModel.setPricePrefix(Utils.getSingletonInstance().convertToCurrencyString(data.getMrp()));
        nsqEntertainmentModel.setUrl("https://www.tokopedia.com/deals/i/" + data.getSeoUrl() + "/");
        nsqEntertainmentModel.setAppUrl("tokopedia://deals/"+data.getSeoUrl());
        nsqRecentDataModel.setNsqEntertainmentModel(nsqEntertainmentModel);
        nsqRecentSearchModel.setNsqRecentDataModel(nsqRecentDataModel);
        nsqTravelRecentSearchModel.setNsqRecentSearchModel(nsqRecentSearchModel);
        postNsqTravelDataUseCase.setTravelDataRequestModel(nsqTravelRecentSearchModel);
        postNsqTravelDataUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Timber.d("enter error");
                throwable.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), () -> sendNsqTravelEvent(userId, data));
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Log.d("Naveen", " Deal Detail NSQ Event 2");
            }
        });
    }


    @Override
    public void getEventContent(@NotNull Function1<? super EventContentData, Unit> onSuccess, @NotNull Function1<? super Throwable, Unit> onError) {
        getEventContentUseCase.getEventContent(onSuccess, onError, this.typeId, String.valueOf(dealsDetailsResponse.getId()));
    }
}
