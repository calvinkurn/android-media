import React from 'react';
import PropTypes from 'prop-types';
import {
  Text,
  View,
  StyleSheet,
  FlatList,
  Image,
  ScrollView,
  Linking,
  TouchableWithoutFeedback
} from 'react-native';
import unescape from 'lodash/unescape'
import { NavigationModule, NetworkModule } from 'NativeModules';
import WishListButton from '../common/Wishlist/WishlistButton';


let ID_User;
const CampaignList = ({ campaigns, User_ID }) => {
  ID_User = User_ID

  return (
    <View style={styles.container}>
      <ScrollView>
        <FlatList
          User_ID={ID_User}
          data={campaigns}
          keyExtractor={item => item.banner_id}
          renderItem={this.renderCampaign}
          initialNumToRender={1}
        />
      </ScrollView>
    </View>
  );
};

this.renderCampaign = (c) => {
  const {
    productCell,
    productImageWrapper,
    productImage,
    productName,
    productGridNormalPrice,
    productGridNormalPriceText,
    priceWrapper,
    price,
    productGridCampaignRate,
    productGridCampaignRateText,
    badgeImageContainer,
    badgeImage,
    productBadgeWrapper,
    productLabel,
    labelText,
    productCashback,
    cashbackText,
    shopSection,
    shopImageWrapper,
    shopImage,
    shopNameWrapper,
    titleText,
    viewAll,
    viewAllText
   } = styles;

  const item = c.item;
  const products = c.item.Products || [];
  const productGrid = [];

  if (products.length > 0) {
    for (let i = 0; i < products.length; i += 2) {
      const productRow = [];
      for (let j = i; j < i + 2; j += 1) {
        const dataProducts = products[j].data;
        if (!products[j]) {
          break;
        }

        productRow.push(
          <View style={productCell} key={dataProducts.id}>
            <TouchableWithoutFeedback onPress={() => {
                console.log("di klik: ", dataProducts.url_app);
                NavigationModule.navigate(dataProducts.url_app, "")
              }}>
              <View>
                <View style={productImageWrapper}>
                  <Image source={{ uri: dataProducts.image_url }} style={productImage} />
                </View>
                <Text
                  style={productName}
                  ellipsizeMode='tail'
                  numberOfLines={2}>{unescape(dataProducts.name)}</Text>
              </View>
            </TouchableWithoutFeedback>
            <View style={productGridNormalPrice}>
              {
                dataProducts.discount_percentage && (
                  <View style={{ height: 3 }}>
                    <Text style={productGridNormalPriceText}>{dataProducts.original_price}</Text>
                  </View>
                )
              }
            </View>
            <View style={priceWrapper}>
              <Text style={price}>{dataProducts.price}</Text>
              { dataProducts.discount_percentage && (
                <View style={productGridCampaignRate}>
                  <Text style={productGridCampaignRateText}>{`${dataProducts.discount_percentage}% OFF`}</Text>
                </View>)
              }
              { dataProducts.badges.map(b => (b.title === 'Free Return' ? (
                <View key={dataProducts.id} style={badgeImageContainer}>
                  <Image source={{ uri: b.image_url }} style={badgeImage} />
                </View>) : null))
              }
            </View>
            <View style={productBadgeWrapper}>
              {
                dataProducts.labels.map((l) => {
                  let labelTitle = l.title;
                  // console.log(l);
                  if (l.title.indexOf('Cashback') > -1) {
                    labelTitle = 'Cashback';
                  }
                  switch (labelTitle) {
                    case 'PO':
                    case 'Grosir':
                      return (
                        <View style={productLabel} key={dataProducts.id}>
                          <Text style={labelText}>{l.title}</Text>
                        </View>);
                    case 'Cashback':
                      return (
                        <View style={productCashback} key={dataProducts.id}>
                          <Text style={cashbackText}>{l.title}</Text>
                        </View>
                      );
                    default:
                      return null;
                  }
                })
              }
            </View>
            <TouchableWithoutFeedback onPress={() => {
              console.log("Shop: ", dataProducts.shop.url_app)
              NavigationModule.navigate(dataProducts.shop.url_app, "")}}>
              <View style={shopSection}>
                <View style={shopImageWrapper}>
                  <Image source={{ uri: products[j].brand_logo }} style={shopImage} />
                </View>
                <View style={shopNameWrapper}>
                  <Text
                    style={{ lineHeight: 15 }}
                    ellipsizeMode='tail'
                    numberOfLines={1}>{unescape(dataProducts.shop.name)}</Text>
                </View>
              </View>
            </TouchableWithoutFeedback>
            <WishListButton
              User_ID={ID_User}
              isWishlist={dataProducts.is_wishlist || false}
              productId={dataProducts.id} />
          </View>
        );
      }
      productGrid.push(
        <View style={styles.productRow} key={i}>
          {productRow}
        </View>
      );
    }
  }

  return (
    <View style={{ marginBottom: 10, backgroundColor: '#fff', borderTopWidth: 1, borderColor: '#e0e0e0' }}>
      {
        item.html_id === 6 ? null : <Text style={titleText}>{item.title}</Text>
      }
      {
        item.html_id === 6 ? (
          <TouchableWithoutFeedback onPress={() => {
            console.log('banner: ', item.redirect_url_app)
            NavigationModule.navigate(item.redirect_url_desktop, item.redirect_url_mobile, "")}}>
            <Image source={{ uri: item.image_url }} style={{ height: 80 }} />
          </TouchableWithoutFeedback>
        ) :
          (
            <TouchableWithoutFeedback onPress={() => {
              console.log('[banner] ' + item.redirect_url_app )
              NavigationModule.navigateWithMobileUrl(item.redirect_url_app, item.redirect_url_mobile, "")}}>
              <Image source={{ uri: item.mobile_url }} style={{ height: 110 }} />
            </TouchableWithoutFeedback>
          )
      }
      {productGrid}
      {
        item.html_id === 6 ? null : (<View style={viewAll}>
          <Text
            style={viewAllText}
            onPress={() => {
              console.log('Lihat semua: ', item.redirect_url_app)
              NavigationModule.navigate(item.redirect_url_app, '')}}>Lihat Semua &gt;
          </Text>
        </View>)
      }
    </View >
  );
};

this.onClick = () => {

};

CampaignList.propTypes = {
  campaigns: PropTypes.arrayOf(PropTypes.object).isRequired
};

const styles = StyleSheet.create({
  container: {
  },
  titleText: {
    fontSize: 16,
    fontWeight: '600',
    margin: 10
  },
  productRow: {
    flex: 1,
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderColor: '#e0e0e0'
  },
  productCell: {
    flex: 1 / 2,
    borderRightWidth: 1,
    borderColor: '#e0e0e0'
  },
  priceWrapper: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    height: 34
  },
  price: {
    color: '#ff5722',
    fontSize: 13,
    fontWeight: '600',
    lineHeight: 20,
    paddingHorizontal: 10
  },
  productName: {
    fontSize: 13,
    fontWeight: '600',
    color: 'rgba(0,0,0,.7)',
    height: 33.8,
    paddingHorizontal: 10
  },
  productImageWrapper: {
    borderBottomWidth: 1,
    borderColor: 'rgba(255,255,255,0)',
    padding: 10
  },
  productImage: {
    height: 185,
    borderRadius: 3
  },
  productBadgeWrapper: {
    height: 27,
    paddingVertical: 5,
    paddingHorizontal: 10,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center'
  },
  productCashback: {
    borderRadius: 3,
    marginRight: 3,
    padding: 3,
    backgroundColor: '#42b549'
  },
  cashbackText: {
    color: '#fff',
    fontSize: 10
  },
  shopSection: {
    flex: 1,
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderColor: '#e0e0e0',
    justifyContent: 'center'
  },
  shopImage: {
    width: 28,
    height: 28
  },
  shopImageWrapper: {
    width: 30,
    height: 30,
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  shopNameWrapper: {
    flex: 3 / 4,
    marginTop: 7,
    marginLeft: 10,
    marginBottom: 5,
    marginRight: 0
  },
  viewAll: {
    paddingVertical: 15,
    paddingHorizontal: 10,
    borderBottomWidth: 1,
    borderColor: '#e0e0e0',
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'flex-end'
  },
  viewAllText: {
    color: '#42b549',
    fontSize: 13
  },
  productLabel: {
    padding: 3,
    borderColor: '#e0e0e0',
    borderWidth: 1,
    marginRight: 3,
    backgroundColor: '#fff',
    borderRadius: 3
  },
  labelText: {
    fontSize: 10
  },
  productGridPrice: {
    height: 34
  },
  productGridNormalPrice: {
    paddingHorizontal: 10
  },
  productGridNormalPriceText: {
    fontSize: 10,
    fontWeight: '600',
    textDecorationLine: 'line-through'
  },
  badgeImageContainer: {
    paddingHorizontal: 10,
    left: 15
  },
  badgeImage: {
    height: 16,
    width: 16
  },
  productGridCampaignRate: {
    backgroundColor: '#ff5722',
    padding: 3,
    borderRadius: 3,
    marginLeft: 5
  },
  productGridCampaignRateText: {
    color: '#fff',
    fontSize: 11,
    textAlign: 'center'
  }
});

export default CampaignList;
