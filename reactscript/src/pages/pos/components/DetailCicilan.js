import React, { Component } from 'react'
import { View, Text, TouchableOpacity, StyleSheet, TouchableWithoutFeedback, Image } from 'react-native'
import { NavigationModule } from 'NativeModules'


class DetailCicilan extends Component {
    onBackPress = () => {
        NavigationModule.finish()
    }

    render() {
        const { titleTxt } = styles
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
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3 }}>
                        <Text style={{ 
                            marginTop: 10, 
                            fontSize: 21, 
                            fontWeight: '600', 
                            textAlign: 'center' }}>Bank</Text>
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
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-ANZ.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>ANZ</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-BCA.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>BCA</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-BNI.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>BNI</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-BRI.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>BRI</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-HSBC.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>HSBC</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Mandiri.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Mandiri</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Maybank.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Maybank</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Danamon.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Danamon</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-MNC.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>MNC Bank</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-OCBCNISP.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>OCBC</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Panin.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Panin Bank</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Permata.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Permata</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-Standard.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>Standard Chartered</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                <View style={{ 
                    marginTop: 15,
                    marginBottom: 15,
                    justifyContent: 'space-around',
                    flexDirection: 'row' }}>
                    <View style={{ flex: 3, flexDirection: 'row', alignContent: 'center' }}>
                        <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/Logo-UOB.png' }} 
                            style={{  width: 50, resizeMode: 'contain', marginLeft: 30 }} />
                        <Text style={{ fontSize: 20, fontWeight: '200', marginLeft: 20 }}>UOB</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                    <View style={{ flex: 1 }}>
                        <Text style={titleTxt}>-</Text>
                    </View>
                </View>
                
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

export default DetailCicilan