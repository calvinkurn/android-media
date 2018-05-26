package com.tokopedia.digital_deals.view.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.view.contractor.SelectQuantityContract;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.oms.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.oms.domain.model.request.cart.CartItem;
import com.tokopedia.oms.domain.model.request.cart.CartItems;
import com.tokopedia.oms.domain.model.request.cart.Configuration;
import com.tokopedia.oms.domain.model.request.cart.MetaData;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.oms.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;


public class SelectQuantityPresenter
        extends BaseDaggerPresenter<SelectQuantityContract.View>
        implements SelectQuantityContract.Presenter {

    private PostVerifyCartUseCase postVerifyCartUseCase;
    private String promocode="";


    @Inject
    public SelectQuantityPresenter(PostVerifyCartUseCase postVerifyCartUseCase) {
        this.postVerifyCartUseCase = postVerifyCartUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }



    private JsonObject convertPackageToCartItem(PackageViewModel packageViewModel) {
        Configuration config = new Configuration();
        config.setPrice(packageViewModel.getSalesPrice() * packageViewModel.getSelectedQuantity());
        MetaData meta = new MetaData();
        meta.setEntityCategoryId(packageViewModel.getCategoryId());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setTotalTicketPrice(packageViewModel.getSalesPrice() * packageViewModel.getSelectedQuantity());


        meta.setEntityStartTime("");


        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setMetaData(meta);
        cartItem.setConfiguration(config);
        cartItem.setQuantity(packageViewModel.getSelectedQuantity());
        cartItem.setProductId(packageViewModel.getProductId());


        cartItems.add(cartItem);
        CartItems cart = new CartItems();
        cart.setCartItems(cartItems);
        cart.setPromocode(promocode);

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cart));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return requestBody;
    }



    public void verifyCart(PackageViewModel checkoutData) {
        getView().showProgressBar();
        final RequestParams params = RequestParams.create();
        params.putObject(Utils.Constants.CHECKOUTDATA, convertPackageToCartItem(checkoutData));
        postVerifyCartUseCase.execute(params, new Subscriber<VerifyCartResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ReviewTicketPresenter", "onError");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                verifyCart(checkoutData);
                            }
                        });
            }

            @Override
            public void onNext(VerifyCartResponse verifyCartResponse) {
                Log.d("ReviewTicketPresenter", verifyCartResponse.toString());

//                if (!isPromoCodeCase) {
//                    if ("failure".equals(verifyCartResponse.getStatus().getResult())) {
//                        getView().hideProgressBar();
//                        getView().showMessage("Silahkan Isi Data Pelanggan Tambahan");
//                    } else {
//                        paymentparams = RequestParams.create();
//                        if (selectedSeatViewModel != null) {
//                            EntityPackagesItem entityPackagesItem = verifyCartResponse.getCart().getCartItems().get(0).getMetaData().getEntityPackages().get(0);
//                            entityPackagesItem.setSeatIds(selectedSeatViewModel.getSeatIds());
//                            entityPackagesItem.setSeatPhysicalRowIds(selectedSeatViewModel.getPhysicalRowIds());
//                            entityPackagesItem.setSeatRowIds(selectedSeatViewModel.getSeatRowIds());
//                            entityPackagesItem.setActualSeatNos(selectedSeatViewModel.getActualSeatNos());
//                        }
//                        paymentparams.putObject("verfiedcart", verifyCartResponse.getCart());
//                        getPaymentLink();
//                    }
//                } else {
//                    String errorMsg = verifyCartResponse.getCart().getPromocodeFailureMessage();
//                    if (errorMsg != null &&
//                            errorMsg.length() > 0) {
//                        getView().hideProgressBar();
//                        getView().hideSuccessMessage();
//                        getView().showPromoSuccessMessage(errorMsg,
//                                getView().getActivity().getResources().getColor(R.color.red_a700));
//                        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_FAILURE + promocode, errorMsg);
//                        promocode = "";
//                    } else {
//                        String successMsg = verifyCartResponse.getCart().getPromocodeSuccessMessage();
//                        if (successMsg != null && successMsg.length() > 0) {
//                            getView().hideProgressBar();
//                            getView().showPromoSuccessMessage(getView().getActivity().getResources().getString(R.string.promo_success_msg),
//                                    getView().getActivity().getResources().getColor(R.color.black_54));
//                        }
//                    }
//                }
            }
        });
    }



}
