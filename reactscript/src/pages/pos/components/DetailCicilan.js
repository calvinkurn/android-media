import React, { Component } from 'react'
import { View, TouchableOpacity, StyleSheet, Image } from 'react-native'
import { Text } from '../common/TKPText'
import { NavigationModule } from 'NativeModules'
import { connect } from 'react-redux'
import { getBankList } from '../actions/index'

class DetailCicilan extends Component {
  onBackPress = () => {
    NavigationModule.finish()
  }

  componentDidMount() {
    this.props.dispatch(getBankList())
  }

  render() {
    const { titleTxt } = styles
    const banks = this.props.banks
    console.log(banks)
    const termMap = [3, 6, 12, 18, 24]
    return (
      <View>
        <View style={styles.headerContainer}>
          <View>
            <TouchableOpacity onPress={() => this.onBackPress()}>
              <Image
                style={{ width: 20, height: 20, resizeMode: 'contain' }}
                source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/icon_back.png' }} />
            </TouchableOpacity>
          </View>
          <View style={{ left: -220 }}>
            <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Detail Cicilan</Text>
          </View>
          <View></View>
        </View>

        <View style={{
          marginTop: 30,
          marginBottom: 20,
          justifyContent: 'space-around',
          flexDirection: 'row'
        }}>
          <View style={{ flex: 3 }}>
            <Text style={{
              marginTop: 10,
              fontSize: 21,
              fontWeight: '600',
              textAlign: 'center'
            }}>Bank</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={titleTxt}>3</Text>
            <Text style={titleTxt}>Bulan</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={titleTxt}>6</Text>
            <Text style={titleTxt}>Bulan</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={titleTxt}>12</Text>
            <Text style={titleTxt}>Bulan</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={titleTxt}>18</Text>
            <Text style={titleTxt}>Bulan</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={titleTxt}>24</Text>
            <Text style={titleTxt}>Bulan</Text>
          </View>
        </View>
        {
          banks.map(b => (
            <View style={{
              marginTop: 15,
              marginBottom: 15,
              justifyContent: 'space-around',
              flexDirection: 'row',
            }} key={b.bank_id}>
              <View style={{ flex: 3, flexDirection: 'row' }}>
                <Image source={{ uri: b.bank_logo || 'http://via.placeholder.com/50x27' }}
                  style={{ width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>{b.bank_name}</Text>
              </View>
              {
                termMap.map(t => {
                  let view = (<View style={{ flex: 1 }} key={t}>
                    <Text style={titleTxt}>-</Text>
                  </View>)

                  b.installment_list.map(i => {
                    const hasTermPlan = (t == i.term)
                    if (hasTermPlan) {
                      view = (
                        <View style={{ flex: 1 }} key={t}>
                          <Text style={titleTxt}>√</Text>
                        </View>
                      )
                    }
                  })

                  return view
                })
              }
            </View>
          ))
        }
      </View>
    )
  }
}

const styles = StyleSheet.create({
  titleTxt: {
    fontSize: 17, fontWeight: '600', textAlign: 'center'
  },
  headerContainer: {
    height: 75,
    backgroundColor: '#42b549',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: '4%',
  },
})

const mapStateToProps = (state, ownProps) => ({
  banks: state.payment.items
})

export default connect(mapStateToProps)(DetailCicilan)