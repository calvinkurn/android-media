package com.tokopedia.digital.common.data.mapper;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.OperatorBuilder;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.Promo;
import com.tokopedia.common_digital.product.presentation.model.Rule;
import com.tokopedia.digital.common.data.entity.response.Field;
import com.tokopedia.digital.common.data.entity.response.GuideEntity;
import com.tokopedia.digital.common.data.entity.response.OperatorBannerEntity;
import com.tokopedia.digital.common.data.entity.response.RechargeCategoryDetail;
import com.tokopedia.digital.common.data.entity.response.RechargeFavoritNumber;
import com.tokopedia.digital.common.data.entity.response.RechargeFavoritNumberResponseEntity;
import com.tokopedia.digital.common.data.entity.response.RechargeResponseEntity;
import com.tokopedia.digital.common.data.entity.response.Validation;
import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.view.model.AdditionalFeature;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalMapper {

    public ProductDigitalData transformCategoryData(RechargeResponseEntity entity) throws MapperDataException {
        CategoryData categoryData = new CategoryData();
        if (entity != null && entity.getRechargeCategoryDetail() != null) {
            RechargeCategoryDetail categoryDetail = entity.getRechargeCategoryDetail();

            categoryData.setTitleText(categoryDetail.getTitle());
            categoryData.setCategoryId(categoryDetail.getId());
            categoryData.setDefaultOperatorId(categoryDetail.getDefaultOperatorId());
            categoryData.setIcon(categoryDetail.getIcon());
            categoryData.setIconUrl(categoryDetail.getIconUrl());
            categoryData.setName(categoryDetail.getName());
            categoryData.setInstantCheckout(categoryDetail.getInstantCheckout());
            categoryData.setNew(categoryDetail.getIsNew());
            categoryData.setSlug(categoryDetail.getSlug());
            categoryData.setOperatorStyle(categoryDetail.getOperatorStyle());
            categoryData.setOperatorLabel(categoryDetail.getOperatorLabel());
            if (categoryDetail.getAdditionalFeature() != null) {
                categoryData.setAdditionalFeature(transformAdditionalFeature(categoryDetail.getAdditionalFeature()));
            }

            categoryData.setClientNumberList(transformClientNumberList(categoryDetail));
            categoryData.setOperatorList(transformOperators(categoryDetail));
            categoryData.setBannerDataListIncluded(transformBanners(categoryDetail.getBanners()));
            categoryData.setOtherBannerDataListIncluded(transformBanners(categoryDetail.getOtherBanners()));
            categoryData.setGuideDataList(transformGuides(categoryDetail.getGuides()));
        }

        return new ProductDigitalData.Builder()
                .historyClientNumber(new HistoryClientNumber.Builder()
                        .lastOrderClientNumber(getLastOrder(entity.getRechargeFavoritNumberResponseEntity()))
                        .recentClientNumberList(getOrderList(entity.getRechargeFavoritNumberResponseEntity()))
                        .build())
                .categoryData(categoryData)
                .bannerDataList(categoryData.getBannerDataListIncluded())
                .otherBannerDataList(categoryData.getOtherBannerDataListIncluded())
                .guideDataList(categoryData.getGuideDataList())
                .build();
    }

    private AdditionalFeature transformAdditionalFeature(
            com.tokopedia.digital.common.data.entity.response.AdditionalFeature additionalFeature) {
        return new AdditionalFeature(additionalFeature.getText(), additionalFeature.getButtonText(),
                additionalFeature.getId());
    }

    /**
     * Helper function to transfor client number list
     *
     * @param entity
     * @return
     */
    private List<ClientNumber> transformClientNumberList(RechargeCategoryDetail entity) {
        List<ClientNumber> clientNumberCategoryList = new ArrayList<>();

        if (entity != null && entity.getClientNumber() != null && entity.getClientNumber().getName().equalsIgnoreCase(ClientNumber.DEFAULT_TYPE_CONTRACT)) {
            ClientNumber clientNumberCategory = new ClientNumber();
            clientNumberCategory.setName(entity.getClientNumber().getName());
            clientNumberCategory.set_default(entity.getClientNumber().getDefault());
            clientNumberCategory.setType(entity.getClientNumber().getType());
            clientNumberCategory.setPlaceholder(entity.getClientNumber().getPlaceholder());
            clientNumberCategory.setText(entity.getClientNumber().getText());
            List<com.tokopedia.common_digital.product.presentation.model.Validation> validationCategoryList
                    = new ArrayList<>();
            for (Validation validation
                    : entity.getClientNumber().getValidation()) {
                com.tokopedia.common_digital.product.presentation.model.Validation validationCategory =
                        new com.tokopedia.common_digital.product.presentation.model.Validation();
                validationCategory.setError(validation.getError());
                validationCategory.setRegex(validation.getRegex());
                validationCategoryList.add(validationCategory);
            }
            clientNumberCategory.setValidation(validationCategoryList);
            clientNumberCategoryList.add(clientNumberCategory);
        }

        return clientNumberCategoryList;
    }

    /**
     * Helper function to transform operator list
     *
     * @param entity
     * @return
     */
    private List<Operator> transformOperators(RechargeCategoryDetail entity) {
        List<Operator> operatorCategoryList = new ArrayList<>();

        if (entity == null) return operatorCategoryList;

        for (OperatorBannerEntity categoryDetailIncluded : entity.getOperators()) {
            String name = categoryDetailIncluded.getAttributes().getName();
            int defaultProductId = categoryDetailIncluded.getAttributes().getDefaultProductId();
            String image = categoryDetailIncluded.getAttributes().getImage();
            String ussdCode = categoryDetailIncluded.getAttributes().getUssd();
            String lastOrderUrl = categoryDetailIncluded.getAttributes().getLastorderUrl();
            String operatorId = categoryDetailIncluded.getId();
            List<String> prefixList = categoryDetailIncluded.getAttributes().getPrefix();
            String operatorType = categoryDetailIncluded.getType();
            List<Product> products = new ArrayList<>();
            for (com.tokopedia.digital.common.data.entity.response.Product product
                    : categoryDetailIncluded.getAttributes().getProduct()) {
                if (product.getAttributes().getStatus() != Product.STATUS_INACTIVE) {
                    Product productOperator = new Product();
                    productOperator.setDesc(product.getAttributes().getDesc());
                    productOperator.setDetail(product.getAttributes().getDetail());
                    productOperator.setDetailCompact(product.getAttributes().getDetailCompact());
                    productOperator.setDetailUrl(product.getAttributes().getDetailUrl());
                    productOperator.setDetailUrlText(product.getAttributes().getDetailUrlText());
                    productOperator.setInfo(product.getAttributes().getInfo());
                    productOperator.setPrice(product.getAttributes().getPrice());
                    productOperator.setPricePlain(product.getAttributes().getPricePlain());
                    productOperator.setProductType(product.getType());
                    productOperator.setProductId(product.getId());
                    productOperator.setStatus(product.getAttributes().getStatus());
                    if (product.getAttributes().getPromo() != null) {
                        Promo productPromo = new Promo();
                        productPromo.setBonusText(product.getAttributes().getPromo().getBonusText());
                        productPromo.setId(product.getAttributes().getPromo().getId());
                        productPromo.setNewPrice(product.getAttributes().getPromo().getNewPrice());
                        productPromo.setNewPricePlain(
                                product.getAttributes().getPromo().getNewPricePlain()
                        );
                        productPromo.setTag(product.getAttributes().getPromo().getTag());
                        productPromo.setTerms(product.getAttributes().getPromo().getTerms());
                        productPromo.setValueText(product.getAttributes().getPromo().getValueText());
                        productOperator.setPromo(productPromo);
                    }
                    products.add(productOperator);
                }
            }
            List<ClientNumber> clientNumberList = new ArrayList<>();
            for (Field field
                    : categoryDetailIncluded.getAttributes().getFields()) {
                ClientNumber clientNumberOperator = new ClientNumber();
                clientNumberOperator.setName(field.getName());
                clientNumberOperator.set_default(field.getDefault());
                clientNumberOperator.setType(field.getType());
                clientNumberOperator.setPlaceholder(field.getPlaceholder());
                clientNumberOperator.setText(field.getText());
                List<com.tokopedia.common_digital.product.presentation.model.Validation> validationCategoryList
                        = new ArrayList<>();
                for (com.tokopedia.digital.common.data.entity.response.Validation validation
                        : field.getValidation()) {
                    com.tokopedia.common_digital.product.presentation.model.Validation validationCategory =
                            new com.tokopedia.common_digital.product.presentation.model.Validation();
                    validationCategory.setError(validation.getError());
                    validationCategory.setRegex(validation.getRegex());
                    validationCategoryList.add(validationCategory);
                }
                clientNumberOperator.setValidation(validationCategoryList);
                clientNumberList.add(clientNumberOperator);
            }

            Rule rule = new Rule();
            rule.setMaximumLength(
                    categoryDetailIncluded.getAttributes().getRule().getMaximumLength()
            );
            rule.setEnableVoucher(
                    categoryDetailIncluded.getAttributes().getRule().getEnableVoucher()
            );
            rule.setShowPrice(
                    categoryDetailIncluded.getAttributes().getRule().getShowPrice()
            );
            rule.setProductText(
                    categoryDetailIncluded.getAttributes().getRule().getProductText()
            );
            rule.setProductViewStyle(
                    categoryDetailIncluded.getAttributes().getRule().getProductViewStyle()
            );
            rule.setButtonText(
                    categoryDetailIncluded.getAttributes().getRule().getButtonText()
            );

            Operator operatorCategory = new OperatorBuilder()
                    .name(name)
                    .defaultProductId(defaultProductId)
                    .image(image)
                    .ussdCode(ussdCode)
                    .lastOrderUrl(lastOrderUrl)
                    .operatorId(operatorId)
                    .prefixList(prefixList)
                    .operatorType(operatorType)
                    .products(products)
                    .clientNumberList(clientNumberList)
                    .rule(rule)
                    .createOperator();

            operatorCategoryList.add(operatorCategory);
        }

        return operatorCategoryList;
    }

    /**
     * Helper function to transform banner list
     *
     * @param entity
     * @return
     */
    private List<BannerData> transformBanners(List<OperatorBannerEntity> entity) {
        List<BannerData> bannerDataList = new ArrayList<>();

        if (entity != null) {
            for (OperatorBannerEntity bannerEntity : entity) {
                bannerDataList.add(
                        new BannerData.Builder()
                                .id(bannerEntity.getId())
                                .type(bannerEntity.getType())
                                .title(bannerEntity.getAttributes().getTitle())
                                .subtitle(bannerEntity.getAttributes().getSubTitle())
                                .promocode(bannerEntity.getAttributes().getPromocode())
                                .image(bannerEntity.getAttributes().getImage())
                                .dataTitle(bannerEntity.getAttributes().getDataTitle())
                                .link(bannerEntity.getAttributes().getLink())
                                .build()
                );
            }
        }

        return bannerDataList;
    }

    private OrderClientNumber getLastOrder(RechargeFavoritNumberResponseEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderClientNumber orderClientNumber = new OrderClientNumber();

        orderClientNumber.setCategoryId(entity.getCategoryId());
        orderClientNumber.setClientNumber(entity.getClientNumber());
        orderClientNumber.setOperatorId(entity.getOperatorId());
        orderClientNumber.setProductId(entity.getProductId());

        return orderClientNumber;
    }


    private List<OrderClientNumber> getOrderList(RechargeFavoritNumberResponseEntity entity) {
        List<OrderClientNumber> clientNumbers = new ArrayList<>();

        if (entity != null && entity.getList() != null) {

            for (RechargeFavoritNumber favoritNumber : entity.getList()) {
                OrderClientNumber orderClientNumber = new OrderClientNumber();

                orderClientNumber.setCategoryId(favoritNumber.getCategoryId());
                orderClientNumber.setClientNumber(favoritNumber.getClientNumber());
                orderClientNumber.setOperatorId(favoritNumber.getOperatorId());
                orderClientNumber.setProductId(favoritNumber.getProductId());
                orderClientNumber.setName(favoritNumber.getLabel());

                clientNumbers.add(orderClientNumber);
            }
        }

        return clientNumbers;
    }

    /**
     * Helper function to transform guides list
     *
     * @param entityList
     * @return
     */
    private List<GuideData> transformGuides(List<GuideEntity> entityList) {
        List<GuideData> guideDataList = new ArrayList<>();

        if (entityList != null) {
            for (GuideEntity item : entityList) {
                guideDataList.add(
                        new GuideData.Builder()
                                .id(item.getId())
                                .type(item.getType())
                                .title(item.getAttribute().getTitle())
                                .sourceLink(item.getAttribute().getSourceLink())
                                .build()
                );
            }
        }

        return guideDataList;
    }
}
