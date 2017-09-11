import React, { Component } from 'react'
import PropTypes from 'prop-types'
import {
  ViewPagerAndroid,
  Platform,
  View,
  Dimensions,
  Image,
  StyleSheet,
  TouchableWithoutFeedback,
  Text,
} from 'react-native'
import { NetworkModule, NavigationModule } from 'NativeModules';
import Swiper from 'react-native-swiper'
// import Carousel from 'react-native-looped-carousel';

const { height, width } = Dimensions.get('window')

const BannerList = ({ banners, onBannerPress, onViewAllPress }) => {
  const topBanners = banners.filter(banner => banner.html_id === 0)
  return (
    <View height={215} backgroundColor={'rgba(0, 0, 0, 0.05)'} paddingBottom={10}>
      <Swiper 
        autoplay={true}
        showsPagination={true}
        autoplayTimeout={5}
        height={205}
        style={styles.bannerSwipe}
        paginationStyle={styles.bannerPagination}
        activeDotColor={'#FF5722'}
        >
        {
          topBanners.map((banner, index) => (
            <TouchableWithoutFeedback key={index} onPress={(e) => onBannerPress(e, banner)}>
              <View key={banner.banner_id} style={styles.bannerBox}>
                <Image source={{ uri: banner.image_url }} style={styles.pageStyle} />
              </View>
            </TouchableWithoutFeedback>
          ))
        }
      </Swiper>
      <Text
        style={styles.viewAll}
        onPress={onViewAllPress}> Lihat Semua Promo  >
      </Text>
    </View>
  )
}

var styles = StyleSheet.create({
  container: {
    marginVertical: 10
  },
  bannerBox: {
    width: width,
    height: 180
  },
  pageStyle: {
    alignItems: 'center',
    padding: 20,
    width: width,
    height: 173,
    height: 180,
    resizeMode: 'contain',
  },
  viewPager: {
    height: 185,
  },
  viewAll: {
    color: '#42b549',
    fontSize: 12,
    fontWeight: '600',
    textAlign: 'right',
    padding: 10,
    position: 'absolute',
    bottom: 0,
    right: 0,
  },
  bannerPagination: {
    justifyContent: 'flex-start',
    position: 'absolute',
    width: 210,
    left: 10,
    bottom: 0,
  },
  slide: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  }
})

BannerList.propTypes = {
  banners: PropTypes.array,
  onBannerPress: PropTypes.func,
  onViewAllPress: PropTypes.func,
}

export default BannerList



// const BannerList = ({ banners, onBannerPress, onViewAllPress }) => {
//   const topBanners = banners.filter(banner => banner.html_id === 0)
//   return (
//     <View style={{flex: 1,}}>
//       <Carousel
//         delay={5000}
//         style={{width, height: 200}}
//         bullets={true}
//       >
//         {
//           topBanners.map((banner, index) => (
//             <TouchableWithoutFeedback key={index} onPress={(e) => onBannerPress(e, banner)}>
//               <View key={banner.banner_id} style={styles.bannerContainer}>
//                 <Image source={{ uri: banner.image_url }} style={styles.bannerImage} />
//               </View>
//             </TouchableWithoutFeedback>
//           ))
//         }
//       </Carousel>
//       <Text
//         style={styles.viewAll}
//         onPress={onViewAllPress}> Lihat Semua Promo  >
//       </Text>
//     </View>
//   )
// }

// var styles = StyleSheet.create({
//   bannerContainer: {
//     flex: 1
//   },
//   bannerImage: {
//     alignItems: 'center',
//     width: width,
//     height: Math.round(width * 0.5),
//     resizeMode: 'contain',
//   },
//   viewAll: {
//     position: 'absolute',
//     bottom: 0,
//     right: 0,
//     color: '#42b549',
//     fontSize: 12,
//     fontWeight: '600',
//     textAlign: 'right',
//     padding: 10
//   },
//   slide: {
//     flex: 1,
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   bannerPagination: {
//     justifyContent: 'flex-start',
//     position: 'absolute',
//     width: 210,
//     left: 10,
//     bottom: 0,
//   }
// })

