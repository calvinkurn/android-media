import React, { Component } from 'react'
import { View, StyleSheet, Image, Text, Dimensions } from 'react-native'
import { connect } from 'react-redux'
import { icons } from '../../../icons/index'


const { height, width, fontScale } = Dimensions.get('window');
const scale = width / 375
const infoContent = [
    {
      name: 'Produk dari Brand Resmi',
      desc: 'Semua produk di Official Store Tokopedia merupakan produk langsung dari brand-brand pilihan.',
      img: icons.infographic_original,
      // img: 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/infographic-original.png?alt=media&token=1bde4c36-e251-4049-aec8-f924b897a650',
    },
    {
      name: 'Pelayanan Berkualitas untuk Anda',
      desc: 'Tim Customer Care kami selalu siap untuk melayani Anda selama 24/7.',
      img: icons.infographic_service,
      // img: 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/infographic-service.png?alt=media&token=7472cdc5-70d2-41b9-993c-e76f60312b5e',
    },
    {
      name: 'Penawaran Promo Ekslusif',
      desc: 'Dapatkan penawaran ekslusif mulai dari diskon, cashback, hingga buy 1 get 1 free.',
      img: icons.infographic_promotion,
      // img: 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/infographic-promotion.png?alt=media&token=92072d69-1148-46a2-b67a-91706764dd8b',
    },
    {
      name: 'Cicilan 0% Gratis Biaya Admin',
      desc: 'Cicilan bunga 0% dan bebas biaya admin untuk tenor 3, 6, 12, 18, sampai 24 bulan.',
      img: icons.infographic_free_installment,
      // img: 'https://firebasestorage.googleapis.com/v0/b/tokopedia-android.appspot.com/o/infographic-free-installment.png?alt=media&token=8d0a9339-c2e6-49f2-81a1-e22c1d73c450'
    }]


class infographic extends Component {
  renderInfographic = () => {
    return (
      <View style={styles.osInfographic}>
        {infoContent.map((info, idx) => (
          <View style={styles.osInfoContent} key={idx}>
            <View style={styles.osInfoImgWrap}>
              <Image source={ info.img } style={{width:70, height:70, resizeMode:'contain'}} />
            </View>
            <View style={styles.osInfoContentText}>
              <View>
                <Text style={styles.osInfoHeading}>{info.name}</Text>
              </View>
              <View style={styles.osInfoContentPara}>
                <Text
                  ellipsizeMode='tail'
                  numberOfLines={3}
                  style={styles.osInfoContentParaText}>{info.desc}</Text>
              </View>
            </View>
          </View>
        ))}
      </View>
    )
  }

  render() {
    return(
      <View>
        {!this.props.campaigns.isFetching && this.renderInfographic()}
      </View>
    )
  }
} 

const mapStateToProps = state => {
  const { campaigns, brands } = state
  return {
    campaigns,
    brands
  }
}


const styles = StyleSheet.create({
  osInfographic: {
    backgroundColor: '#FFF',
    marginTop: 20,
    paddingRight: 10,
    paddingBottom: 10,
    paddingLeft: 10,
    backgroundColor: '#FFF',
    borderTopWidth: 1,
    borderColor: '#E0E0E0',
    width: width,
  },
  osInfoContent: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  osInfoImgWrap: {
    display: 'flex',
    flexDirection: 'row',
    width: 80,
    justifyContent: 'center',
  },
  osInfoImg: {
    width: 75,
    resizeMode: 'contain',
  },
  osInfoContentText: {
    flex: 1,
    paddingLeft: 10,
  },
  osInfoHeading: {
    fontSize: Math.round(scale * 14),
    fontWeight: '600',
    marginTop: 5,
  },
  osInfoContentPara: {
    marginTop: 5,
    margin: 0,
  },
  osInfoContentParaText: {
    fontSize: 12,
    lineHeight: 16,
    textAlign: 'auto',
  }
})

export default connect(mapStateToProps)(infographic)