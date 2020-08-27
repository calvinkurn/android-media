package com.tokopedia.home.account.data.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator;
import com.tokopedia.home.account.data.util.StaticBuyerModelGeneratorUoh;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.navigation_common.model.VccUserStatus;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.VccStatus.ACTIVE;
import static com.tokopedia.home.account.AccountConstants.VccStatus.BLOCKED;
import static com.tokopedia.home.account.AccountConstants.VccStatus.DEACTIVATED;
import static com.tokopedia.home.account.AccountConstants.VccStatus.ELIGIBLE;
import static com.tokopedia.home.account.AccountConstants.VccStatus.HOLD;
import static com.tokopedia.home.account.AccountConstants.VccStatus.KYC_PENDING;
import static com.tokopedia.home.account.AccountConstants.VccStatus.REJECTED;

/**
 * @author by alvinatin on 10/08/18.
 */

public class BuyerAccountMapper implements Func1<AccountModel, BuyerViewModel> {
    private static final String OVO = "OVO";
    private static final String OVO_PAY_LATER = "OVO PayLater";
    private static final String LABEL_ELIGIBLE = "Aktifkan";
    private static final String LABEL_HOLD = "Sedang Diproses";
    private static final String LABEL_BLOCKED = "Layanan Terblokir";
    private static final String LABEL_DEACTIVATED = "Dinonaktifkan";
    private static final String LABEL_KYC_PENDING = "Selesaikan Pengajuan Aplikasimu";
    private static final String UOH_AB_TEST_KEY = "UOH_android";
    private Context context;
    private RemoteConfig remoteConfig;
    private UserSession userSession;

    @Inject
    BuyerAccountMapper(@ApplicationContext Context context, RemoteConfig remoteConfig, UserSession userSession) {
        this.context = context;
        this.remoteConfig = remoteConfig;
        this.userSession = userSession;
    }

    @Override
    public BuyerViewModel call(AccountModel accountModel) {
        return getBuyerModel(context, accountModel);
    }

    private BuyerViewModel getBuyerModel(Context context, AccountModel accountModel) {
        BuyerViewModel model = new BuyerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        if (accountModel.getProfile() != null) {
            items.add(getBuyerProfileMenu(accountModel));
        }

        String cdnUrl = AccountHomeUrl.CDN_URL;
        if (context.getApplicationContext() instanceof AccountHomeRouter) {
            cdnUrl = ((AccountHomeRouter) context.getApplicationContext())
                    .getStringRemoteConfig(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL);
        }

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        if (accountModel.getWallet() != null) {
            tokopediaPayViewModel.setLinked(accountModel.getWallet().isLinked());
            tokopediaPayViewModel.setWalletType(accountModel.getWallet().getWalletType());
            if (accountModel.getWallet().getWalletType().equals(OVO)) {
                tokopediaPayViewModel.setIconUrlLeft(cdnUrl + AccountHomeUrl.ImageUrl.OVO_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    if (accountModel.getWallet().getAmountPendingCashback() > 0) {
                        tokopediaPayViewModel.setLabelLeft("(+" + accountModel.getWallet().getPendingCashback() + ")");
                    } else {
                        tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    }

                    if (accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft("Points " + accountModel.getWallet().getPointBalance());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getCashBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            } else {
                tokopediaPayViewModel.setIconUrlLeft(cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    if (accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            }
        }

        if ((accountModel.getSaldoModel() != null &&
                accountModel.getSaldoModel().getSaldo() != null) ||
                (accountModel.getVccUserStatus() != null && accountModel.getVccUserStatus().getStatus() != null &&
                        accountModel.getVccUserStatus().getStatus().equalsIgnoreCase((AccountConstants.VccStatus.REJECTED)))) {

            tokopediaPayViewModel.setIconUrlRight(cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG);
            tokopediaPayViewModel.setLabelRight(context.getString(R.string.label_tokopedia_pay_deposit));
            tokopediaPayViewModel.setRightSaldo(true);
            tokopediaPayViewModel.setAmountRight(CurrencyFormatUtil.convertPriceValueToIdrFormat
                    (accountModel.getSaldoModel().getSaldo().getDepositLong(), true));

            tokopediaPayViewModel.setApplinkRight(ApplinkConstInternalGlobal.SALDO_DEPOSIT);
            items.add(tokopediaPayViewModel);

        }

        if (accountModel.getVccUserStatus() != null &&
                accountModel.getVccUserStatus().getTitle() != null &&
                accountModel.getVccUserStatus().getTitle().equalsIgnoreCase(OVO_PAY_LATER)) {
            VccUserStatus vccUserStatus = accountModel.getVccUserStatus();
            TokopediaPayBSModel tokopediaPayBSModel = new TokopediaPayBSModel();

            tokopediaPayViewModel.setIconUrlCentre(vccUserStatus.getIcon());
            tokopediaPayViewModel.setApplinkCentre(vccUserStatus.getRedirectionUrl());

            tokopediaPayViewModel.setAmountCentre(accountModel.getVccUserStatus().getBody());

            switch (vccUserStatus.getStatus()) {
                case ELIGIBLE:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_ELIGIBLE);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case HOLD:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_HOLD);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case ACTIVE:
                    tokopediaPayViewModel.setLabelCentre(vccUserStatus.getTitle());
                    String oplLimit;
                    try {
                        oplLimit = CurrencyFormatUtil.convertPriceValueToIdrFormat(Long.parseLong(vccUserStatus.getBody()), true);
                    } catch (Exception e) {
                        oplLimit = vccUserStatus.getBody();
                    }
                    tokopediaPayViewModel.setAmountCentre(oplLimit);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case REJECTED:
                    tokopediaPayViewModel.setLabelCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setBsDataCentre(null);
                    break;
                case BLOCKED:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_BLOCKED);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case DEACTIVATED:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_DEACTIVATED);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;
                case KYC_PENDING:
                    tokopediaPayViewModel.setAmountCentre(vccUserStatus.getTitle());
                    tokopediaPayViewModel.setLabelCentre(LABEL_KYC_PENDING);
                    tokopediaPayViewModel.setBsDataCentre(tokopediaPayBSModel);
                    break;

            }

        } else {
            tokopediaPayViewModel.setBsDataCentre(null);
        }

        if (useUoh()) {
            items.addAll(StaticBuyerModelGeneratorUoh.Companion.getModel(context, accountModel, remoteConfig, accountModel.getUohOrderCount()));
        } else {
            items.addAll(StaticBuyerModelGenerator.Companion.getModel(context, accountModel, remoteConfig));
        }
        model.setItems(items);

        return model;
    }

    private BuyerCardViewModel getBuyerProfileMenu(AccountModel accountModel) {
        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setUserId(accountModel.getProfile().getUserId());
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());

        if (accountModel.getTokopoints() != null
                && accountModel.getTokopoints().getStatus() != null
                && accountModel.getTokopoints().getStatus().getPoints() != null) {
            buyerCardViewModel.setTokopoint(accountModel.getTokopoints().getStatus().getPoints().getRewardStr());
            buyerCardViewModel.setEggImageUrl(accountModel.getTokopoints().getStatus().getTier().getImageUrl());
        }

        if (accountModel.getTokopointsSumCoupon() != null) {
            buyerCardViewModel.setCoupons(accountModel.getTokopointsSumCoupon().getSumCouponStr());
        }

        if (accountModel.getMembershipSumUserCard() != null) {
            buyerCardViewModel.setTokomember(accountModel.getMembershipSumUserCard().getSumUserCardStr());
        }

        buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
        if (accountModel.getProfile().getCompletion() != null) {
            buyerCardViewModel.setProgress(accountModel.getUserProfileCompletion().getCompletionScore());
        }
        buyerCardViewModel.setAffiliate(accountModel.isAffiliate());

        userSession.setHasPassword(accountModel.getUserProfileCompletion().isCreatedPassword());
        return buyerCardViewModel;
    }

    private Boolean useUoh() {
        String remoteConfigValue = getABTestRemoteConfig().getString(UOH_AB_TEST_KEY);
        return !remoteConfigValue.isEmpty();
    }

    private RemoteConfig getABTestRemoteConfig() {
        return RemoteConfigInstance.getInstance().getABTestPlatform();
    }
}
