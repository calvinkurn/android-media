import React, { Component } from 'react'
import {
  View,
  Image,
  Dimensions,
  StyleSheet,
  Text,
  TouchableOpacity,
  Platform
} from 'react-native'



const { width, height } = Dimensions.get('window')

const TopBanner = ({ navigation, dataTopBanners }) => {
  const banners = dataTopBanners
  const renderBanner = () => {
    return (
      <View style={styles.topBannerContainer}>
        <Image source={{uri: banners.images[3].file_url}} style={styles.topBannerImage}/>
      </View>
    )
  }

  const renderTerms = () => {
    return (
      <View style={styles.termsContainer}>
        <View style={[styles.termsRow, {borderTopWidth: 1}]}>
          <View style={[styles.termsWrap, styles.rightBorder]}>
            <Text>Periode Promo</Text>
            <Text style={styles.termContent}>{banners.period}</Text>
          </View>
          <View style={styles.termsWrap}>
            <Text>Minimum Transaksi</Text>
            <Text style={styles.termContent}>{banners.min_transaction_str}</Text>
          </View>
        </View>
        <View style={styles.termsRow}>
          <View style={[styles.termsWrap, styles.rightBorder]}>
            <Text>Kode Promo</Text>
            <Text style={styles.termContent}>{banners.promo_code}</Text>
          </View>
          <View style={styles.termsWrap}>
            <TouchableOpacity
              onPress={() => navigation.navigate('TermsPage', { termsCondition: dataTopBanners.terms_conditions })}>
              <View style={styles.tcWrap}>
                <Text style={{color: '#42b549', textAlign: 'center'}}>Syarat & Ketentuan</Text>
              </View>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    )
  }

  return (
    <View style={{marginBottom: 20}}>
      {renderBanner()}
      {renderTerms()}
    </View>
  )
}

const styles = StyleSheet.create({
  topBannerContainer: {
    flex: 1,
    width,
    height: Math.round(width * 0.4),
  },
  topBannerImage: {
    height: Math.round(width * 0.4),
    resizeMode: 'cover',
  },
  termsContainer: {
    flexDirection: 'column',
  },
  termsRow: {
    flex: 1,
    width,
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderColor: '#E0E0E0'
  },
  termContent: {
    marginTop: 3,
    fontWeight: '600',
    textAlign: 'center',
  },
  rightBorder: {
    borderRightWidth: 1,
    borderColor: '#E0E0E0',
  },
  termsWrap: {
    flex: 1 / 2,
    height: 70,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFF',
  },
  textPeriod: {
    width: Math.round((width / 2) * .85),
    textAlign: 'center'
  },
  tcWrap: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  }
})

export default TopBanner