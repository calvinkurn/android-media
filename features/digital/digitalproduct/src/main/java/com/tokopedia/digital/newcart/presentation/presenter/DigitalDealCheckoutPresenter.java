package com.tokopedia.digital.newcart.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.contract.DigitalDealCheckoutContract;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DigitalDealCheckoutPresenter extends DigitalBaseCartPresenter<DigitalDealCheckoutContract.View>
        implements DigitalDealCheckoutContract.Presenter {
    private static final long AUTO_COLLAPSE_ANIMATION_DELAY = 2500;
    private DigitalAnalytics digitalAnalytics;
    private UserSession userSession;

    @Inject
    public DigitalDealCheckoutPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase,
                                        DigitalAnalytics digitalAnalytics,
                                        DigitalModuleRouter digitalModuleRouter,
                                        ICartDigitalInteractor cartDigitalInteractor,
                                        UserSession userSession,
                                        DigitalCheckoutUseCase digitalCheckoutUseCase,
                                        DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase,
                                        DigitalPostPaidLocalCache digitalPostPaidLocalCache) {
        super(digitalAddToCartUseCase,
                digitalAnalytics,
                digitalModuleRouter,
                cartDigitalInteractor,
                userSession,
                digitalCheckoutUseCase,
                digitalInstantCheckoutUseCase,
                digitalPostPaidLocalCache);
        this.digitalAnalytics = digitalAnalytics;
        this.userSession = userSession;
    }

    @Override
    public void onDealsCheckout() {
        getView().setCheckoutParameter(buildCheckoutData(getView().getCartInfoData(), userSession.getAccessToken()));
        renderBaseCart(getView().getCartInfoData());
        getView().renderCategoryInfo(getView().getCartInfoData().getAttributes().getCategoryName());
        if (getView().getCartInfoData().getCrossSellingConfig() != null) {
            getView().updateToolbarTitle(getView().getCartInfoData().getCrossSellingConfig().getHeaderTitle());
            getView().updateCheckoutButtonText(getView().getCartInfoData().getCrossSellingConfig().getCheckoutButtonText());
            if (getView().getCartInfoData().getCrossSellingConfig().isSkipAble()) {
                getView().renderSkipToCheckoutMenu();
            }
        } else {
            getView().updateToolbarTitle(R.string.digital_deal_toolbar_title);
        }

        if (getView().isAlreadyShowOnBoard()){
            autoCollapseCheckoutView();
        }
    }

    @Override
    public void autoCollapseCheckoutView() {
        Observable.timer(AUTO_COLLAPSE_ANIMATION_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (isViewAttached()) {
                            if (getView().isCartDetailViewVisible() && !getView().isAlreadyCollapsByUser()) {
                                getView().hideCartDetailView();
                                getView().hideDealsContainerView();
                                getView().renderIconToExpand();
                            }
                        }
                    }
                });
    }

    @Override
    public void onExpandCollapseButtonView() {
        if (getView().isCartDetailViewVisible()) {
            getView().hideCartDetailView();
            getView().hideDealsContainerView();
            getView().renderIconToExpand();
        } else {
            getView().showCartDetailView();
            if (getView().getSelectedDeals().size() > 0) {
                getView().showDealsContainerView();
            }else {
                getView().hideDealsContainerView();
            }
            getView().renderIconToCollapse();
        }
    }

    @Override
    public void onNewSelectedDeal(DealProductViewModel viewModel) {
        if (getView().isCartDetailViewVisible() && !getView().isAlreadyCollapsByUser()){
            getView().hideCartDetailView();
            getView().hideDealsContainerView();
            getView().renderIconToExpand();
        }
        getView().addSelectedDeal(viewModel);
        getView().renderCartDealListView(viewModel);
        getView().renderCategoryInfo(
                String.format("%s & %d Deals", getView().getCartInfoData().getAttributes().getCategoryName(), getView().getSelectedDeals().size())
        );
        long discountPlain = getView().getCheckoutDiscountPricePlain();
        long total = calculateRechargeAndDealsTotal();
        if (discountPlain > 0) {
            long totalWithDiscount = total - discountPlain;
            List<CartAdditionalInfo> additionals = new ArrayList<>(getView().getCartInfoData().getAdditionalInfos());
            List<CartItemDigital> items = new ArrayList<>();
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_cost_label), getStringIdrFormat((double) total)));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_promo_label), String.format("-%s", getStringIdrFormat((double) discountPlain))));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_total_cost_label), getStringIdrFormat((double) totalWithDiscount)));
            CartAdditionalInfo cartAdditionalInfo = new CartAdditionalInfo(getView().getString(R.string.digital_cart_additional_payment_label), items);
            additionals.add(cartAdditionalInfo);
            getView().renderAdditionalInfo(additionals);
        }
        getView().renderCheckoutView(total);
    }

    @Override
    protected void renderIfHasDiscount(VoucherDigital voucherDigital) {
        long total = calculateRechargeAndDealsTotal();
        if (voucherDigital.getAttributeVoucher().getDiscountAmountPlain() > 0) {
            long discountPlain = voucherDigital.getAttributeVoucher().getDiscountAmountPlain();
            long totalWithDiscount = total - discountPlain;
            List<CartAdditionalInfo> additionals = new ArrayList<>(getView().getCartInfoData().getAdditionalInfos());
            List<CartItemDigital> items = new ArrayList<>();
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_cost_label), getStringIdrFormat((double) total)));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_promo_label), String.format("-%s", getStringIdrFormat((double) discountPlain))));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_total_cost_label), getStringIdrFormat((double) totalWithDiscount)));
            CartAdditionalInfo cartAdditionalInfo = new CartAdditionalInfo(getView().getString(R.string.digital_cart_additional_payment_label), items);
            additionals.add(cartAdditionalInfo);
            getView().renderAdditionalInfo(additionals);
            getView().expandAdditionalInfo();
            getView().enableVoucherDiscount(
                    voucherDigital.getAttributeVoucher().getDiscountAmountPlain()
            );
        }
    }

    private long calculateRechargeAndDealsTotal() {
        int totalDeals = 0;
        for (DealProductViewModel viewModel1 : getView().getSelectedDeals()) {
            totalDeals += viewModel1.getSalesPriceNumeric();
        }
        return getView().getCartInfoData().getAttributes().getPricePlain() + totalDeals;
    }

    @Override
    public void onDealRemoved(DealProductViewModel viewModel) {
        digitalAnalytics.eventRemoveDeal(viewModel);
        int indexOf = getView().getSelectedDeals().indexOf(viewModel);
        if (indexOf != -1) {
            getView().getSelectedDeals().remove(indexOf);
            getView().notifySelectedDealListView(viewModel);
        }

        getView().updateDealListView(viewModel);
        if (getView().getSelectedDeals().size() == 0) {
            getView().hideDealsContainerView();
        }
        if (getView().getSelectedDeals().size() > 0) {
            getView().renderCategoryInfo(
                    String.format("%s & %d Deals", getView().getCartInfoData().getAttributes().getCategoryName(), getView().getSelectedDeals().size())
            );
        } else {
            getView().renderCategoryInfo(getView().getCartInfoData().getAttributes().getCategoryName());
        }

        long discountPlain = getView().getCheckoutDiscountPricePlain();
        long total = calculateRechargeAndDealsTotal();
        if (discountPlain > 0) {
            long totalWithDiscount = total - discountPlain;
            List<CartAdditionalInfo> additionals = new ArrayList<>(getView().getCartInfoData().getAdditionalInfos());
            List<CartItemDigital> items = new ArrayList<>();
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_cost_label), getStringIdrFormat((double) total)));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_promo_label), String.format("-%s", getStringIdrFormat((double) discountPlain))));
            items.add(new CartItemDigital(getView().getString(R.string.digital_cart_additional_payment_total_cost_label), getStringIdrFormat((double) totalWithDiscount)));
            CartAdditionalInfo cartAdditionalInfo = new CartAdditionalInfo(getView().getString(R.string.digital_cart_additional_payment_label), items);
            additionals.add(cartAdditionalInfo);
            getView().renderAdditionalInfo(additionals);
        }
        getView().renderCheckoutView(total);
    }

    @Override
    protected void renderCouponAndVoucher(VoucherDigital voucherDigital) {
        super.renderCouponAndVoucher(voucherDigital);
        getView().showPromoOnlyForTopUpAndBillMessage();
    }

    @Override
    public void onSkipMenuClicked() {
        digitalAnalytics.eventSkipDeal();
        processToCheckout();
    }

    @Override
    public void onDealDetailClicked(DealProductViewModel productViewModel) {
        getView().navigateToDealDetailPage(productViewModel.getUrl());
    }

    private List<Integer> getDealIds() {
        List<Integer> dealIds = new ArrayList<>();
        for (DealProductViewModel viewModel : getView().getSelectedDeals()) {
            dealIds.add((int) viewModel.getId());
        }
        return dealIds;
    }

    @NonNull
    @Override
    protected RequestBodyCheckout getRequestBodyCheckout(CheckoutDataParameter checkoutData) {
        RequestBodyCheckout requestBodyCheckout = super.getRequestBodyCheckout(checkoutData);
        requestBodyCheckout.getAttributes().setDealsIds(getDealIds());
        return requestBodyCheckout;
    }

    @Override
    public void processToCheckout() {
        super.processToCheckout();
        digitalAnalytics.eventAddDeal(
                getView().getCartInfoData().getAttributes().getCategoryName(),
                getView().getCheckoutDataParameter().build().getVoucherCode(),
                getView().getSelectedDeals().size()
        );
    }
}
