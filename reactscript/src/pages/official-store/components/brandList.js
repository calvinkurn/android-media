import React, { Component } from 'react'
import PropTypes from 'prop-types'
import {
  View,
  Text,
  Image,
  ScrollView,
  StyleSheet,
  Button,
  TouchableWithoutFeedback,
  Linking
} from 'react-native'
import { NetworkModule, NavigationModule } from 'NativeModules';
import unescape from 'lodash/unescape'
import LoadMore from './LoadMore'
import Grid from '../common/grid/grid'
import FavouriteBtn from '../common/Favourite/favBtn'
import WishlistBtn from '../common/Wishlist/WishlistButton'


const BrandList = (props) => {
  const gridProps = {
    User_ID: props.User_ID,
    rows: 3,
    columns: 3,
    data: props.gridData,
    onLoadMore: props.loadMore,
    onSlideMore: props.slideMore,
    limit: props.limit,
    offset: props.offset,
    isFetching: props.isFetching,
    canFetch: props.canFetch
  }

  const validBrands = props.brands.filter(brand => brand && brand.microsite_url && brand.products.length && brand.logo_url)
  
  return (
    <View>
      <Grid {...gridProps} />
      {
        validBrands.map(b => (
          <View key={b.id} style={styles.brandContainer}>
            <View style={styles.shopHeadContainer}>
              <View style={styles.shopImageWrapper}>
                <TouchableWithoutFeedback onPress={() => NavigationModule.navigate(b.shop_apps_url, '')}>
                  <Image style={styles.shopImage} source={{ uri: b.logo_url }} />
                </TouchableWithoutFeedback>
              </View>
              <TouchableWithoutFeedback onPress={() => NavigationModule.navigate(b.shop_apps_url, '')}>
                <View style={styles.shopNameBox}>
                  <Text
                    style={styles.shopName}
                    ellipsizeMode='tail'
                    numberOfLines={1}>
                    {unescape(b.name)}
                  </Text>
                </View>
              </TouchableWithoutFeedback>
              <View style={{ marginLeft: 'auto', flexDirection: 'row', alignItems: 'center' }}>
                <FavouriteBtn
                  User_ID={props.User_ID}
                  shopId={b.id}
                  isFav={b.isFav} />
              </View>
            </View>
            <View style={styles.productsWrapper}>
              <ScrollView
                horizontal={true}
                automaticallyAdjustContentInsets={false}
                showsHorizontalScrollIndicator={false}>
                {
                  b.products.map(p => (
                    <View style={styles.thumb} key={p.id}>
                      <TouchableWithoutFeedback onPress={() => {
                        console.log("[brandList] Product: ", p)
                        NavigationModule.navigate(p.url_app, '')}}>
                        <View>
                          <Image style={styles.productImage} source={{ uri: p.image_url }} />
                          <Text style={styles.productName}
                            ellipsizeMode='tail'
                            numberOfLines={2}>{unescape(p.name)}
                          </Text>
                        </View>
                      </TouchableWithoutFeedback>
                      <View style={styles.originalPriceContainer}>
                        {
                          p.discount_percentage && (
                            <Text style={styles.originalPriceText}>{p.original_price}</Text>
                          )
                        }
                      </View>
                      <View style={styles.productAttributeContainer}>
                        <Text style={styles.price} >{p.price}</Text>
                        {
                          p.discount_percentage && (
                            <View style={styles.productGridCampaignRate}>
                              <Text style={styles.productGridCampaignRateText}>{`${p.discount_percentage}% OFF`}</Text>
                            </View>
                          )
                        }
                        {
                          p.badges.map((b, i) => b.title === 'Free Return' ? (
                            <View key={i}>
                              <Image source={{ uri: b.image_url }} style={styles.badgeImage} />
                            </View>
                          ) : null)
                        }
                      </View>
                      <View style={styles.label}>
                        {
                          p.labels.map((l, index) => {
                            let labelTitle = l.title
                            if (l.title.indexOf('Cashback') > -1) {
                              labelTitle = 'Cashback'
                            }
                            const key = `${p.id}-${labelTitle}`
                            switch (labelTitle) {
                              case 'PO':
                              case 'Grosir':
                                return (
                                  <View style={styles.productLabel} key={index}>
                                    <Text style={styles.labelText}>{l.title}</Text>
                                  </View>)
                              case 'Cashback':
                                return null
                              default:
                                return null
                            }
                          })
                        }
                      </View>
                      <WishlistBtn
                        User_ID={props.User_ID}
                        isWishlist={p.is_wishlist} 
                        productId={p.id} />
                    </View>
                  ))
                }
              </ScrollView>
            </View>
          </View>
        ))
      }
      {
        !props.isFetching && (
          <LoadMore
            User_ID={props.User_ID}
            onLoadMore={props.loadMore}
            offset={props.offset}
            limit={props.limit}
            canFetch={props.canFetch}
            isFetching={props.isFetching} />
        )
      }
    </View>
  )
}

const styles = StyleSheet.create({
  brandContainer: {
    backgroundColor: '#fff',
    borderTopWidth: 1,
    marginBottom: 10,
    borderColor: '#e0e0e0',
  },
  thumb: {
    padding: 7,
    borderRightWidth: 1,
    borderColor: '#e0e0e0',
  },
  productImage: {
    height: 135,
    width: 135,
  },
  shopHeadContainer: {
    borderBottomWidth: 1,
    borderColor: '#e0e0e0',
    flex: 1,
    flexDirection: 'row',
    padding: 10,
  },
  shopImageWrapper: {
    borderWidth: 1,
    borderRadius: 3,
    borderColor: '#e0e0e0',
  },
  shopImage: {
    width: 50,
    height: 50,
    borderRadius: 3,
  },
  shopName: {
    marginTop: 5,//'5 5 8 10'
    marginRight: 5,
    marginBottom: 8,
    marginLeft: 10,
    fontWeight: "600",
    fontSize: 14,
    color: 'rgba(0,0,0,.7)'
  },
  productsWrapper: {
    borderBottomWidth: 1,
    borderColor: '#e0e0e0',
    backgroundColor: '#fff',
  },
  price: {
    color: '#ff5722',
    fontSize: 13,
    lineHeight: 18,
  },
  productAttributeContainer: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  productName: {
    width: 135,
    height: 40,
    marginTop: 5
  },
  label: {
    flex: 1,
    flexDirection: 'row',
    marginTop: 5,
  },
  shopNameBox: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  productLabel: {
    padding: 3,
    borderColor: '#e0e0e0',
    borderWidth: 1,
    marginRight: 3,
    padding: 3,
    backgroundColor: '#fff',
    borderRadius: 3,
  },
  labelText: {
    fontSize: 10,
  },
  badgeImage: {
    height: 16,
    width: 16,
    alignSelf: 'flex-end'
  },
  originalPriceText: {
    fontSize: 10,
    fontWeight: '600',
    textDecorationLine: 'line-through',
  },
  productGridCampaignRate: {
    backgroundColor: '#ff5722',
    padding: 3,
    borderRadius: 3,
    marginLeft: 5,
  },
    productGridCampaignRateText: {
    color: '#fff',
    fontSize: 11,
    textAlign: 'center',
  }
})

export default BrandList