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
  Linking,
} from 'react-native'
import Swiper from 'react-native-swiper'


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
    //marginVertical: 10
  },
  bannerBox: {
    width: width,
    height: 180
  },
  pageStyle: {
    alignItems: 'center',
    width: width,
    height: 180,
    resizeMode: 'contain',
  },
  viewAll: {
    position: 'absolute',
    bottom: 0,
    right: 0,
    color: '#42b549',
    fontSize: 12,
    fontWeight: '600',
    textAlign: 'right',
    padding: 10
  },
  slide: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  bannerPagination: {
    justifyContent: 'flex-start',
    position: 'absolute',
    width: 210,
    left: 10,
    bottom: 0,
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
//     <View style={{flex: 1}}>
//       <Carousel
//         delay={5000}
//         style={styles.carouselContainer}
//         bullets={true}
//         bulletsContainerStyle={styles.dotContainer}
//         bulletStyle={styles.dot}
//         chosenBulletStyle={styles.dotActive}
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
//       <View style={styles.viewAllContainer}>
//         <Text
//           style={styles.viewAll}
//           onPress={onViewAllPress}> Lihat Semua Promo  >
//         </Text>
//       </View>
//     </View>
//   )
// }

// var styles = StyleSheet.create({
//   carouselContainer: {
//     width,
//     height: Math.round(width * 0.453) + 40,
//   },
//   bannerContainer: {
//     height: 200,
//   },
//   bannerImage: {
//     alignItems: 'center',
//     width: width,
//     height: Math.round(width * 0.46),
//     resizeMode: 'contain',
//   },
//   viewAllContainer: {
//     position: 'absolute',
//     bottom: 10,
//     right: 10,
//     flex: 1 / 2,
//     alignSelf: 'flex-end',
//     width: 140,
//     height: 20,
//   },
//   viewAll: {
//     color: '#42b549',
//     fontSize: 12,
//     fontWeight: '600',
//     textAlign: 'right',
//   },
//   slide: {
//     flex: 1,
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   dotContainer: {
//     position: 'absolute',
//     left: 0,
//     bottom: 0,
//     height: 20,
//     marginLeft: 10,
//   },
//   dot: {
//     width: 8,
//     height: 8,
//     backgroundColor: 'rgba(0, 0, 0, .2)',
//     borderWidth: 0,
//     margin: 3,
//     borderWidth: 0,
//   },
//   dotActive: {
//     margin: 3,
//     width: 8,
//     height: 8,
//     borderRadius: 20,
//     backgroundColor: '#FF5722',
//     borderWidth: 0,
//   },
// })

// BannerList.propTypes = {
//   banners: PropTypes.array,
//   onBannerPress: PropTypes.func,
//   onViewAllPress: PropTypes.func,
// }

// export default BannerList

